package net.gegy1000.wearables.server.item;

import net.gegy1000.wearables.server.api.item.RegisterItemModel;
import net.gegy1000.wearables.server.block.BlockRegistry;
import net.gegy1000.wearables.server.block.WearableAssemblerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WearableAssemblerItem extends ItemBlock implements RegisterItemModel {
    public WearableAssemblerItem() {
        super(BlockRegistry.WEARABLE_ASSEMBLER);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (!block.isReplaceable(world, pos)) {
                pos = pos.offset(facing);
            }
            if (player.canPlayerEdit(pos, facing, stack) && this.block.canPlaceBlockAt(world, pos)) {
                this.place(world, pos, EnumFacing.fromAngle(player.rotationYaw), this.block);
                SoundType soundType = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
                world.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                stack.stackSize--;
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    private void place(World world, BlockPos pos, EnumFacing facing, Block block) {
        BlockPos top = pos.up();
        IBlockState state = block.getDefaultState().withProperty(WearableAssemblerBlock.FACING, facing);
        world.setBlockState(pos, state.withProperty(WearableAssemblerBlock.HALF, WearableAssemblerBlock.Half.LOWER), 2);
        world.setBlockState(top, state.withProperty(WearableAssemblerBlock.HALF, WearableAssemblerBlock.Half.UPPER), 2);
        world.notifyNeighborsOfStateChange(pos, block);
        world.notifyNeighborsOfStateChange(top, block);
    }
}
