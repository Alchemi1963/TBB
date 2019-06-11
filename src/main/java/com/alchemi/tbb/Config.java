package com.alchemi.tbb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.configurations.SexyConfiguration;
import me.alchemi.al.objects.base.ConfigBase;

public class Config extends ConfigBase {

	public Config() throws FileNotFoundException, IOException, InvalidConfigurationException {
		super(main.getInstance());
	}
	
	public static enum ConfigEnum implements IConfigEnum{
		CONFIG(new File(main.getInstance().getDataFolder(), "config.yml"), 0),
		MESSAGES(new File(main.getInstance().getDataFolder(), "messages.yml"), 0),
		MAPS(new File(main.getInstance().getDataFolder(), "maps.yml"), 0);
		
		final int version;
		final File file;
		SexyConfiguration config;
		
		private ConfigEnum(File file, int version) {
			this.version = version;
			this.file = file;
			this.config = SexyConfiguration.loadConfiguration(file);
		}
		
		@Override
		public SexyConfiguration getConfig() {
			return config;
		}

		@Override
		public File getFile() {
			return file;
		}

		@Override
		public int getVersion() {
			return version;
		}
		
	}
	
	public static enum OPTIONS implements IConfig{
		;

		@Override
		public boolean asBoolean() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int asInt() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public ItemStack asItemStack() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Material asMaterial() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Sound asSound() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String asString() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> asStringList() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void get() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public SexyConfiguration getConfig() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String key() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object value() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static enum Messages implements IMessage{
		MAPS_CREATED("TheBeastBelow.Maps.Created"),
		MAPS_DELETED("TheBeastBelow.Maps.Deleted");
		
		String value;
		String key;

		private Messages(String key) {
			this.key = key;
		}

		public void get() {
			value = getConfig().getString(key, "PLACEHOLDER - STRING NOT FOUND");
		}
  
		public String value() { return value; }

		@Override
		public String key() {
			return key;
		}

		@Override
		public SexyConfiguration getConfig() {
			return ConfigEnum.MESSAGES.getConfig();
		} 
	}
	
	@Override
	protected IConfigEnum[] getConfigs() {

		return ConfigEnum.values();
	}

	@Override
	protected Set<IConfig> getEnums() {
		return new HashSet<IConfig>();
	}

	@Override
	protected Set<IMessage> getMessages() {
		return new HashSet<IMessage>() {
			{
				addAll(Arrays.asList(Messages.values()));
			}
		};
	}

}
