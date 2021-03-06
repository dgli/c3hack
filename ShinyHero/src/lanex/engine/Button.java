package lanex.engine;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;

public class Button {
	protected static final TrueTypeFont TEXT_FONT = new TrueTypeFont(new Font("sans-serif", Font.PLAIN, 14), true);
	
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected String img;
	protected Color hoverColor;
	protected boolean hovered = false;

	public Button(float x, float y, float width, float height, String img, Color hoverColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.img = img;
		this.hoverColor = hoverColor;
	}

	public boolean ifOnButton(float mx, float my) {
		if (mx > x && mx < x + width && my > y && my < y + height){
			//ERM.sfx("button.se.ogg");
			return true;
		}
		return false;
	}
	
	public boolean updateHoverStatus(float mx, float my) {
		hovered = ifOnButton(mx, my);
		return hovered;
	}

	public void render(Graphics g) {
		if (ERM.getImage(img) != null) {
			if (hovered) {
				ERM.getImage(img).draw(x, y, hoverColor);
			} else {
				ERM.getImage(img).draw(x, y);
			}
		} else {
			//clipping for text overflow
			g.setClip((int)x, (int)y, (int)width, (int)height);
			if (hovered) {
				g.setColor(hoverColor);
			} else {
				g.setColor(Color.white);
			}
			g.fillRect(x, y, width, height);
			TEXT_FONT.drawString(x + 20, y, img, Color.black);
			g.clearClip();
		}
	}
}