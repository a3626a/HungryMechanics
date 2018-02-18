package oortcloud.hungrymechanics.multiblock;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class MultiBlocks {
	private static final List<EnumFacing> EMPTY = Lists.newArrayList();
	@SuppressWarnings("unchecked")
	public static final MultiBlockInformationRotatable generator = 
			new MultiBlockInformationRotatable (
				new MultiBlockInformation(
						new BlockPos(2,2,4),
						new BlockPos(0,0,0),
						new BlockPos(0,0,0), 
						new boolean[][][] {
							{{true,true,true,true},
							 {true,true,true,true}},
							{{true,true,true,true},
							 {true,true,true,true}}},
						new List[][][] {
							{{EMPTY,EMPTY,EMPTY,EMPTY},
							 {Lists.newArrayList(EnumFacing.UP),EMPTY,EMPTY,EMPTY}},
							{{EMPTY,EMPTY,EMPTY,EMPTY},
							 {Lists.newArrayList(EnumFacing.UP),EMPTY,EMPTY,EMPTY}}}, 
						new List[][][] {
							{{EMPTY,EMPTY,EMPTY,EMPTY},
							 {EMPTY,EMPTY,EMPTY,Lists.newArrayList(EnumFacing.SOUTH)}},
							{{EMPTY,EMPTY,EMPTY,EMPTY},
							 {EMPTY,EMPTY,EMPTY,Lists.newArrayList(EnumFacing.SOUTH)}}}),
				EnumFacing.SOUTH);
}
