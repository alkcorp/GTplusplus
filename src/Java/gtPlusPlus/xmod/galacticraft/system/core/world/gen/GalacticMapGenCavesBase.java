package gtPlusPlus.xmod.galacticraft.system.core.world.gen;

import galaxyspace.SolarSystem.core.registers.blocks.GSBlocks;
import java.util.Random;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GalacticMapGenCavesBase extends MapGenBaseMeta {
	public static final int BREAK_THROUGH_CHANCE = 25;

	protected void generateLargeCaveNode(long par1, int par3, int par4, Block[] blockIdArray, byte[] metaArray,
			double par6, double par8, double par10) {
		this.generateCaveNode(par1, par3, par4, blockIdArray, metaArray, par6, par8, par10,
				1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void generateCaveNode(long par1, int par3, int par4, Block[] blockIdArray, byte[] metaArray, double par6,
			double par8, double par10, float par12, float par13, float par14, int par15, int par16, double par17) {
		double d4 = (double) (par3 * 16 + 8);
		double d5 = (double) (par4 * 16 + 8);
		float f3 = 0.0F;
		float f4 = 0.0F;
		Random random = new Random(par1);
		if (par16 <= 0) {
			int j1 = this.range * 16 - 16;
			par16 = j1 - random.nextInt(j1 / 4);
		}

		boolean flag = false;
		if (par15 == -1) {
			par15 = par16 / 2;
			flag = true;
		}

		int k1 = random.nextInt(par16 / 2) + par16 / 4;

		for (boolean flag1 = random.nextInt(6) == 0; par15 < par16; ++par15) {
			double d6 = 1.5D + (double) (MathHelper.sin((float) par15 * 3.1415927F / (float) par16) * par12 * 1.0F);
			double d7 = d6 * par17;
			float f5 = MathHelper.cos(par14);
			float f6 = MathHelper.sin(par14);
			par6 += (double) (MathHelper.cos(par13) * f5);
			par8 += (double) f6;
			par10 += (double) (MathHelper.sin(par13) * f5);
			if (flag1) {
				par14 *= 0.92F;
			} else {
				par14 *= 0.7F;
			}

			par14 += f4 * 0.1F;
			par13 += f3 * 0.1F;
			f4 *= 0.9F;
			f3 *= 0.75F;
			f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (!flag && par15 == k1 && par12 > 1.0F && par16 > 0) {
				this.generateCaveNode(random.nextLong(), par3, par4, blockIdArray, metaArray, par6, par8, par10,
						random.nextFloat() * 0.5F + 0.5F, par13 - 1.5707964F, par14 / 3.0F, par15, par16, 1.0D);
				this.generateCaveNode(random.nextLong(), par3, par4, blockIdArray, metaArray, par6, par8, par10,
						random.nextFloat() * 0.5F + 0.5F, par13 + 1.5707964F, par14 / 3.0F, par15, par16, 1.0D);
				return;
			}

			if (flag || random.nextInt(4) != 0) {
				double d8 = par6 - d4;
				double d9 = par10 - d5;
				double d10 = (double) (par16 - par15);
				double d11 = (double) (par12 + 2.0F + 16.0F);
				if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
					return;
				}

				if (par6 >= d4 - 16.0D - d6 * 2.0D && par10 >= d5 - 16.0D - d6 * 2.0D && par6 <= d4 + 16.0D + d6 * 2.0D
						&& par10 <= d5 + 16.0D + d6 * 2.0D) {
					int l1 = MathHelper.floor_double(par6 - d6) - par3 * 16 - 1;
					int i2 = MathHelper.floor_double(par6 + d6) - par3 * 16 + 1;
					int j2 = MathHelper.floor_double(par8 - d7) - 1;
					int k2 = MathHelper.floor_double(par8 + d7) + 1;
					int l2 = MathHelper.floor_double(par10 - d6) - par4 * 16 - 1;
					int i3 = MathHelper.floor_double(par10 + d6) - par4 * 16 + 1;
					if (l1 < 0) {
						l1 = 0;
					}

					if (i2 > 16) {
						i2 = 16;
					}

					if (j2 < 1) {
						j2 = 1;
					}

					if (k2 > 120) {
						k2 = 120;
					}

					if (l2 < 0) {
						l2 = 0;
					}

					if (i3 > 16) {
						i3 = 16;
					}

					boolean flag2 = false;

					int localY;
					for (int j3 = l1; j3 < i2; ++j3) {
						for (localY = l2; localY < i3; ++localY) {
							for (int i4 = k2 + 1; i4 >= j2 - 1; --i4) {
								if (i4 >= 0 && i4 < 128 && i4 != j2 - 1 && j3 != l1 && j3 != i2 - 1 && localY != l2
										&& localY != i3 - 1) {
									i4 = j2;
								}
							}
						}
					}

					for (localY = j2; localY < k2; ++localY) {
						double yfactor = ((double) localY + 0.5D - par8) / d7;
						double yfactorSq = yfactor * yfactor;

						for (int localX = l1; localX < i2; ++localX) {
							double zfactor = ((double) (localX + par3 * 16) + 0.5D - par6) / d6;
							double zfactorSq = zfactor * zfactor;

							for (int localZ = l2; localZ < i3; ++localZ) {
								double xfactor = ((double) (localZ + par4 * 16) + 0.5D - par10) / d6;
								double xfactorSq = xfactor * xfactor;
								if (xfactorSq + zfactorSq < 1.0D) {
									int coords = (localX * 16 + localZ) * 256 + localY;
									if (yfactor > -0.7D && xfactorSq + yfactorSq + zfactorSq < 1.0D) {
										if (blockIdArray[coords] == GSBlocks.HaumeaBlocks && metaArray[coords] == 0
												&& random.nextInt(25) == 0) {
											blockIdArray[coords] = Blocks.air;
										} else if (blockIdArray[coords] == GSBlocks.HaumeaBlocks
												&& metaArray[coords] == 0) {
											blockIdArray[coords] = Blocks.air;
										}
									}
								}
							}
						}
					}

					if (flag) {
						break;
					}
				}
			}
		}

	}

	protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, Block[] blockIdArray,
			byte[] metaArray) {
		int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
		if (this.rand.nextInt(15) != 0) {
			var7 = 0;
		}

		for (int var8 = 0; var8 < var7; ++var8) {
			double var9 = (double) (par2 * 16 + this.rand.nextInt(16));
			double var11 = (double) this.rand.nextInt(this.rand.nextInt(120) + 8);
			double var13 = (double) (par3 * 16 + this.rand.nextInt(16));
			int var15 = 1;
			if (this.rand.nextInt(4) == 0) {
				this.generateLargeCaveNode(this.rand.nextLong(), par4, par5, blockIdArray, metaArray, var9, var11,
						var13);
				var15 += this.rand.nextInt(4);
			}

			for (int var16 = 0; var16 < var15; ++var16) {
				float var17 = this.rand.nextFloat() * 3.1415927F * 2.0F;
				float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
				if (this.rand.nextInt(10) == 0) {
					var19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
				}

				this.generateCaveNode(this.rand.nextLong(), par4, par5, blockIdArray, metaArray, var9, var11, var13,
						var19, var17, var18, 0, 0, 1.0D);
			}
		}

	}
}