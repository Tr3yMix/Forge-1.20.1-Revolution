package dev.tr3ymix.revolution.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class UnlitSoulWallTorchBlock extends UnlitWallTorchBlock{

    public UnlitSoulWallTorchBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getIgnitedTorch(BlockState state) {
        return Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(FACING, state.getValue(FACING));
    }
}
