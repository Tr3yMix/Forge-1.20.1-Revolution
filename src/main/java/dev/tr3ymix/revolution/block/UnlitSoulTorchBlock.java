package dev.tr3ymix.revolution.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class UnlitSoulTorchBlock extends UnlitTorchBlock{
    public UnlitSoulTorchBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getIgnitedTorch(BlockState state) {
        return Blocks.SOUL_TORCH.defaultBlockState();
    }
}
