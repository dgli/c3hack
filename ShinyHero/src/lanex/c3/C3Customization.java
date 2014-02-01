package lanex.c3;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import lanex.c3.midi.MusicMap;
import lanex.c3.midi.Track;
import lanex.engine.Button;
import lanex.engine.ButtonList;
import lanex.engine.ERM;
import lanex.engine.ScreenPage;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class C3Customization extends ScreenPage {

	private Button hell_button, menu_button;
	private ButtonList<ButtonList<Track>> songList;
	private ButtonList<Track> trackList;

	private File currentSong;
	private Object currentTrack;

	public C3Customization() {
		hell_button = new Button(C3App.RENDER_WIDTH / 2 + 200, 550, 400, 100,
				"start_button.png");
		menu_button = new Button(C3App.RENDER_WIDTH / 2 - 600, 550, 400, 100,
				"menu_button.png");

		File mid = new File("data/music");
		File[] mids = mid.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".mid");
			}
		});
		songList = new ButtonList<>(10,10,C3App.RENDER_WIDTH/2-15);
		for (File f : mids)
		{
			List<Track> temp = MusicMap.fromPath(f.getPath()).getTrackList();
			trackList = new ButtonList<>(C3App.RENDER_WIDTH/2 + 5,10,C3App.RENDER_WIDTH/2-15); //Temporary to save declaration space. Is nulled later.
			for (Track t : temp)
				trackList.addMember(t, t.toString());
			songList.addMember(trackList, f.getName());
		}
		trackList = null;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		// PROCESSING

		g.clear();

		// ERM.listRes();
		// System.out.println("IMAGE: " + ERM.getImage("room.png"));

		g.drawImage(ERM.getImage("custom_back.png"), 0, 0);
		
		songList.render(g);
		if (trackList != null)
			trackList.render(g);
		hell_button.render(g);
		menu_button.render(g);

		// WIDTH SHOULD BE 400
		// HEIGHT SHOULD BE 100

	}

	@Override
	public void keyPressed(int key, char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int button, int x, int y) {
		// CHECK BUTTONS
		{
			songList.checkButtons(x, y);
			trackList = songList.getSelected();
			if (trackList != null)
				trackList.checkButtons(x, y);
			if (hell_button.ifOnButton(x, y)) {
				C3App.splash.reset();
				C3SplashScreen.setRedirect("game");
				C3App.setPage("splash");
			} else if (menu_button.ifOnButton(x, y)) {
				C3App.splash.reset();
				C3App.setPage("main_menu");
			}
		}

	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO Auto-generated method stub

	}
}
