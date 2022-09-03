package space.quinoaa.testplugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import space.quinoaa.spigotcommons.data.Bounds2i;
import space.quinoaa.spigotcommons.data.Vector2i;
import space.quinoaa.spigotcommons.gui.frame.IFrame;
import space.quinoaa.spigotcommons.gui.frame.component.Button;
import space.quinoaa.spigotcommons.gui.frame.component.Panel;
import space.quinoaa.spigotcommons.util.ItemBuilder;
import space.quinoaa.testplugin.gui.component.Counter;

public class DebugMenu extends IFrame {
	public DebugMenu() {
		super(9 * 3, "Test gui");
		DebugMenu self = this;

		addComponent(
				new Panel(
						new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 7).build()
				),
				new Bounds2i(0, 0, 9, 3),
				-1
		);

		addComponent(
				new Counter(),
				new Vector2i(1, 1)
		);

		addComponent(
				new Button(
						new ItemBuilder(Material.FEATHER).setName("Open Vanilla Inventory").build(),
						plr->openVanilla()
				), new Vector2i(3, 1)
		);

		addComponent(
				new Button(
						new ItemBuilder(Material.PAPER).setName("Open Another Page").build(),
						click->getGuiApi().showGui(getPlayer(), new SecondMenu(self))
				), new Vector2i(4, 1)
		);

		addComponent(
				new Button(
						new ItemBuilder(Material.MAP).setName("Test list").build(),
						click->getGuiApi().showGui(getPlayer(), new ListTest())
				), new Vector2i(6, 1)
		);


	}


	public boolean closed = false;
	@Override
	public void onClose(Player player) {
		super.onClose(player);
		player.sendMessage("Closed");
		closed = true;
	}

	public void openVanilla(){
		getPlayer().openInventory(Bukkit.createInventory(getPlayer(), 9, "test"));
	}
}
