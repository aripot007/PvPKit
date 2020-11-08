package fr.aripot007.pvpkit.api;

import org.json.simpleForBukkit.JSONObject;

import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.OfflinePvPKitPlayer;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;

public class StatsCallHandler implements JSONAPICallHandler {
	
	PvPKitPlayerManager playerMgr = PvPKit.getInstance().getPvPKitPlayerManager();

	@Override
	public Object handle(APIMethodName methodName, Object[] args) {
		
		if(args.length == 0 ) { // Not enough args
			return null;
		}
		OfflinePvPKitPlayer player = playerMgr.getOfflinePlayer((String) args[0]);
		
		if (player == null) return null;
		
		JSONObject o = new JSONObject();
		o.putAll(player.serialize());
		return o;
	}

	@Override
	public boolean willHandle(APIMethodName methodName) {
		return methodName.matches("pvpkit.users.stats");
	}

}
