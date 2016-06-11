package org.jurassicraft.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.api.SubBlocksBlock;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.item.itemblock.JCSlabItemBlock;

public abstract class AncientSlabBlock extends BlockSlab implements SubBlocksBlock
{
    private TreeType type;

    public AncientSlabBlock(TreeType type, IBlockState referenceState)
    {
        super(referenceState.getBlock().getMaterial(referenceState));
        this.type = type;
        IBlockState state = this.blockState.getBaseState();

        if (!this.isDouble())
        {
            state = state.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        Block block = state.getBlock();

        this.setHardness(block.getBlockHardness(state, null, null));
        this.setResistance((block.getExplosionResistance(null) * 5.0F) / 3.0F);
        this.setSoundType(block.getSoundType());
        this.setHarvestLevel(block.getHarvestTool(state), block.getHarvestLevel(state));

        this.setDefaultState(state);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);

        if (!isDouble())
        {
            if ((facing == EnumFacing.UP || (double) hitY <= 0.5D) && facing != EnumFacing.DOWN)
            {
                return state;
            }
            else
            {
                return state.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP);
            }
        }

        return state;
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        return super.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();

        if (!this.isDouble())
        {
            state = state.withProperty(HALF, meta == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM ? 0 : 1;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HALF);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack)
    {
        return null;
    }

    @Override
    public IProperty<?> getVariantProperty()
    {
        return null;
    }

    @Override
    public ItemBlock getItemBlock()
    {
        return new JCSlabItemBlock(this, BlockHandler.INSTANCE.ANCIENT_SLABS.get(type), BlockHandler.INSTANCE.ANCIENT_DOUBLE_SLABS.get(type));
    }
}