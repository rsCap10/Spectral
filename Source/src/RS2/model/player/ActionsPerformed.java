package RS2.model.player;

import RS2.Settings;
import RS2.GameEngine;
import RS2.model.object.Object;
import RS2.util.Misc;
import RS2.util.ScriptManager;

public class ActionsPerformed {

	private Client c;

	public ActionsPerformed(Client Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.actionTimer = 4;
		if (c.actionTimer > 0) {
			return;
		}
		c.actionTimer = 4;
		switch (objectType) {
		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		switch (objectType) {
		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.sendMessage("Object type: " + objectType);
		switch (objectType) {
		}
	}

	public void firstClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch(npcType) {

			case 541:
				c.getShops().openShop(1);
			break;
		}
	}

	public void secondClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcType) {
		}
	}

	public void thirdClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcType) {
		}
	}
}