package dev.tr3ymix.revolution.entity;

import dev.tr3ymix.revolution.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeatEntity extends Entity {

    public SeatEntity(Level level) {
        super(ModEntities.SEAT.get(), level);
        this.noPhysics = true;
    }

    public SeatEntity(Level level, BlockPos pos, double y, Direction direction) {
        this(level);
        this.setPos(Vec3.atBottomCenterOf(pos).add(0, y, 0));
        this.setRot(direction.getOpposite().toYRot(), 0F);
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.level().isClientSide) {
            if(this.getPassengers().isEmpty() || this.level().isEmptyBlock(this.blockPosition())) {
                this.discard();
                this.level().updateNeighbourForOutputSignal(blockPosition(), this.level().getBlockState(this.blockPosition()).getBlock());
            }
        }
    }

    public static boolean sit(Level level, BlockPos position, double yOffset, Player player, Direction direction){
        if(!level.isClientSide){
            List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, new AABB(position));
            if(seats.isEmpty()){
                SeatEntity seat = new SeatEntity(level, position, yOffset, direction);
                level.addFreshEntity(seat);
                return player.startRiding(seat, false);
            }
        }
        return false;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }


    @Override
    protected void addPassenger(@NotNull Entity passenger) {
        super.addPassenger(passenger);
        passenger.setYRot(this.getYRot());
    }

    @Override
    public void onPassengerTurned(@NotNull Entity passenger) {
        setYBodyRotation(passenger);
    }

    private void setYBodyRotation(@NotNull Entity passenger) {
        passenger.setYBodyRot(this.getYRot());
        float wrappedYaw = Mth.wrapDegrees(passenger.getYRot() - this.getYRot());
        float clampedYaw = Mth.clamp(wrappedYaw, -120.0F, 120.0F);
        passenger.yRotO += clampedYaw - wrappedYaw;
        passenger.setYRot(passenger.getYRot() + clampedYaw - wrappedYaw);
        passenger.setYHeadRot(passenger.getYRot());
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity entity)
    {
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};
        for(Direction dir : offsets)
        {
            Vec3 safeVec = DismountHelper.findSafeDismountLocation(entity.getType(), this.level(), this.blockPosition().relative(dir), false);
            if(safeVec != null)
            {
                return safeVec.add(0, 0.25, 0);
            }
        }
        return super.getDismountLocationForPassenger(entity);
    }
}
