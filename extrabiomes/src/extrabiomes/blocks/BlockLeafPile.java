/**
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license
 * located in /MMPL-1.0.txt
 */

package extrabiomes.blocks;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockLeafPile extends Block {

	static private boolean canThisPlantGrowOnThisBlockID(int blockId) {
		return blockId == Block.grass.blockID
				|| blockId == Block.dirt.blockID;
	}

	public BlockLeafPile(int id) {
		super(id, 64, Material.vine);
		final float f = 0.5F;
		final float f1 = 0.015625F;
		setTickRandomly(true);
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
		setStepSound(soundGrassFootstep);
		Block.setBurnProperties(id, 30, 60);
		setTextureFile("/extrabiomes/extrabiomes.png");
	}

	@Override
	public void addCreativeItems(ArrayList itemList) {
		checkNotNull(itemList).add(new ItemStack(this));
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return canThisPlantGrowOnThisBlockID(checkNotNull(world)
				.getBlockId(x, y - 1, z));
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return super.canPlaceBlockAt(checkNotNull(world), x, y, z)
				&& canThisPlantGrowOnThisBlockID(world.getBlockId(x,
						y - 1, z));
	}

	private void checkFlowerChange(World world, int x, int y, int z) {
		if (!canBlockStay(checkNotNull(world), x, y, z)) {
			dropBlockAsItem(world, x, y, z,
					world.getBlockMetadata(x, y, z), 0);
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess iBlockAccess, int x, int y,
			int z)
	{
		return checkNotNull(iBlockAccess).getBiomeGenForCoords(x, z)
				.getBiomeFoliageColor();
	}

	@Override
	public int getBlockColor() {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world,
			int x, int y, int z)
	{
		return null;
	}

	@Override
	public int getRenderColor(int metadata) {
		return getBlockColor();
	}

	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			int neigborId)
	{
		super.onNeighborBlockChange(world, x, y, z, neigborId);
		checkFlowerChange(world, x, y, z);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		checkFlowerChange(world, x, y, z);
	}
}
