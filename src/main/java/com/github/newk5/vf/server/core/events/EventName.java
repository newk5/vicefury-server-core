package com.github.newk5.vf.server.core.events;

public interface EventName {
    String onServerStart = "onServerStart";
    String onServerShutdown = "onServerShutdown";
    String onTick = "onTick";
    String onPlayerJoin = "onPlayerJoin";
    String onPlayerLeave = "onPlayerLeave";
    String onPlayerRequestSpawn = "onPlayerRequestSpawn";
    String onPlayerSpawnScreenSkinChange = "onPlayerSpawnScreenSkinChange";
    String onPlayerSpawn = "onPlayerSpawn";
    String onPlayerDied = "onPlayerDied";
    String onPlayerMessage = "onPlayerMessage";
    String onPlayerCommand = "onPlayerCommand";
    String onDataReceived = "onDataReceived";
    String onPlayerReceiveDamage = "onPlayerReceiveDamage";
    String onPlayerEnterVehicle = "onPlayerEnterVehicle";
    String onPlayerLeaveVehicle = "onPlayerLeaveVehicle";
    String onVehicleCreated = "onVehicleCreated";
    String onVehicleDestroyed = "onVehicleDestroyed";
    String onVehicleExploded = "onVehicleExploded";
    String onVehicleReceiveDamage = "onVehicleReceiveDamage";
    String onNpcCreated = "onNpcCreated";
    String onNpcDestroy = "onNpcDestroy";
    String onNpcSpawned = "onNpcSpawned";
    String onNpcDied = "onNpcDied";
    String onNpcReceiveDamage = "onNpcReceiveDamage";
    String onNpcGainedSightOf = "onNpcGainedSightOf";
    String onNpcLostSightOf = "onNpcLostSightOf";
    String onNpcActionChanged = "onNpcActionChanged";
    String onNpcHeardNoise = "onNpcHeardNoise";
    String onObjectCreated = "onObjectCreated";
    String onObjectDestroyed = "onObjectDestroyed";
    String onObjectReceiveDamage = "onObjectReceiveDamage";
    String onObjectTouched = "onObjectTouched";
    String onObjectBroken = "onObjectBroken";
}