package fr.aripot007.pvpkit.api;

import org.bukkit.Bukkit;
import org.json.simpleForBukkit.JSONObject;

import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;

import fr.aripot007.pvpkit.PvPKit;
import fr.aripot007.pvpkit.game.OfflinePvPKitPlayer;
import fr.aripot007.pvpkit.manager.PvPKitPlayerManager;

public class StatsCallHandler implements JSONAPICallHandler {
	
	PvPKitPlayerManager playerMgr = PvPKit.getInstance().getPvPKitPlayerManager();

	@SuppressWarnings("deprecation")
	@Override
	public Object handle(APIMethodName methodName, Object[] args) {
		
		if(args.length == 0 ) return null; // Not enough args

		String uuid;
		
		if (methodName.getMethodName().equals("name.stats")) {
			
			uuid = Bukkit.getOfflinePlayer((String) args[0]).getUniqueId().toString();
			
		} else if (methodName.getMethodName().equals("uuid.stats")) {
			
			uuid = (String) args[0];
			
		} else {
			return null;
		}
		
		
		OfflinePvPKitPlayer player = playerMgr.getOfflinePlayer(uuid);
		
		if (player == null) return null;
		
		JSONObject o = new JSONObject();
		o.putAll(player.serialize());
		return o;
	}

	@Override
	public boolean willHandle(APIMethodName methodName) {
		
		if(methodName.getNamespace().equals("pvpkit")) {
			return methodName.getMethodName().equals("name.stats") || methodName.getMethodName().equals("uuid.stats");
		}
		
		return false;
		
	}

}
