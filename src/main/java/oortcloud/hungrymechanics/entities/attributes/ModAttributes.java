package oortcloud.hungrymechanics.entities.attributes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import oortcloud.hungrymechanics.core.lib.References;

public class ModAttributes {
	
	public static String NAME_crank_production = References.MODID+".crank_production";
	public static String NAME_crank_weight = References.MODID+".crank_weight";
	
	public static IAttribute crank_production = new RangedAttribute((IAttribute)null, NAME_crank_production, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);
	public static IAttribute crank_weight = new RangedAttribute((IAttribute)null, NAME_crank_weight, 0.0, 0.0, Double.MAX_VALUE).setShouldWatch(false);

	public static Map<String, IAttribute> NAME_MAP; 
	static {
		NAME_MAP = new HashMap<String, IAttribute>();
		NAME_MAP.put(NAME_crank_production, crank_production);
		NAME_MAP.put(NAME_crank_weight, crank_weight);
	}
	
}
