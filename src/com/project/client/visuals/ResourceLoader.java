package com.project.client.visuals;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceLoader {

	public static Image WHITE_STONE, BLACK_STONE, OUTER_DOT, WHITE_STONE_TRANSPARENT, BLACK_STONE_TRANSPARENT;
	public static Image MENU_ICON, SOUND_ICON, SOUND_MUTED_ICON, SETTINGS_ICON, HELP_ICON;
	public static Image ICON;
	
	public void init() throws IOException {
		WHITE_STONE = ImageIO.read(this.getResource("res/icons/WhiteStone2.png"));
		BLACK_STONE = ImageIO.read(this.getResource("res/icons/BlackStone2.png"));
		OUTER_DOT = ImageIO.read(this.getResource("res/icons/OuterDot.png"));
		
		MENU_ICON = ImageIO.read(this.getResource("res/icons/Menu48.png"));
		SOUND_ICON = ImageIO.read(this.getResource("res/icons/Volume48.png"));
		SOUND_MUTED_ICON = ImageIO.read(this.getResource("res/icons/MutedVolume48.png"));
		SETTINGS_ICON = ImageIO.read(this.getResource("res/icons/Settings48.png"));
		HELP_ICON = ImageIO.read(this.getResource("res/icons/QuestionMark48.png"));
		
		ICON = ImageIO.read(this.getResource("res/icons/Icon.png"));
		
		WHITE_STONE_TRANSPARENT = ImageIO.read(this.getResource("res/icons/WhiteStoneTransparent.png"));
		BLACK_STONE_TRANSPARENT = ImageIO.read(this.getResource("res/icons/BlackStoneTransparent.png"));
	}
	
//	public InputStream getResource(String path) {
//		return ResourceLoader.class.getClassLoader().getResourceAsStream(path);
//	}
	
	public File getResource(String path) {
		return new File(path);
	}
}