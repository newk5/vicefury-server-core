package com.github.newk5.vf.server.core.events;

public interface EventName {
    String onTick = "onTick";
    String onLowTPS = "onLowTPS";
    String onServerStart = "onServerStart";
    String onServerShutdown = "onServerShutdown";
    String onPlayerJoin = "onPlayerJoin";
    String onPlayerLeave = "onPlayerLeave";
    String onPlayerRequestSpawn = "onPlayerRequestSpawn";
    String onPlayerSpawnScreenSkinChange = "onPlayerSpawnScreenSkinChange";
    String onPlayerSpawn = "onPlayerSpawn";
    String onPlayerDied = "onPlayerDied";
    String onPlayerDowned = "onPlayerDowned";
    String onPlayerRevived = "onPlayerRevived";
    String onPlayerStartedRevive = "onPlayerStartedRevive";
    String onPlayerFailedRevive = "onPlayerFailedRevive";
    String onPlayerMessage = "onPlayerMessage";
    String onPlayerCommand = "onPlayerCommand";
    String onDataReceived = "onDataReceived";
    String onPlayerReceiveDamage = "onPlayerReceiveDamage";
    String onPlayerEnterVehicle = "onPlayerEnterVehicle";
    String onPlayerLeaveVehicle = "onPlayerLeaveVehicle";
    String onPlayerWeaponReceived = "onPlayerWeaponReceived";
    String onPlayerWeaponRemoved = "onPlayerWeaponRemoved";
    String onPlayerStartedSwimming = "onPlayerStartedSwimming";
    String onPlayerStoppedSwimming = "onPlayerStoppedSwimming";
    String onPlayerDivedUnderwater = "onPlayerDivedUnderwater";
    String onPlayerReachedWaterSurface = "onPlayerReachedWaterSurface";
    String onNpcCreated = "onNpcCreated";
    String onNpcDestroy = "onNpcDestroy";
    String onNpcSpawned = "onNpcSpawned";
    String onNpcDied = "onNpcDied";
    String onNpcDowned = "onNpcDowned";
    String onNpcRevived = "onNpcRevived";
    String onNpcStartedRevive = "onNpcStartedRevive";
    String onNpcFailedRevive = "onNpcFailedRevive";
    String onNpcReceiveDamage = "onNpcReceiveDamage";
    String onNpcEnterVehicle = "onNpcEnterVehicle";
    String onNpcLeaveVehicle = "onNpcLeaveVehicle";
    String onNpcWeaponReceived = "onNpcWeaponReceived";
    String onNpcWeaponRemoved = "onNpcWeaponRemoved";
    String onNpcRanOutOfAmmo = "onNpcRanOutOfAmmo";
    String onNpcIsAboutToAttack = "onNpcIsAboutToAttack";
    String onNpcActionChanged = "onNpcActionChanged";
    String onNpcGainedSightOf = "onNpcGainedSightOf";
    String onNpcLostSightOf = "onNpcLostSightOf";
    String onNpcHeardNoise = "onNpcHeardNoise";
    String onNpcReachedLocation = "onNpcReachedLocation";
    String onNpcReachedFollowTarget = "onNpcReachedFollowTarget";
    String onNpcStartedSwimming = "onNpcStartedSwimming";
    String onNpcStoppedSwimming = "onNpcStoppedSwimming";
    String onNpcDivedUnderwater = "onNpcDivedUnderwater";
    String onNpcReachedWaterSurface = "onNpcReachedWaterSurface";
    String onNpcEnemyDied = "onNpcEnemyDied";
    String onNpcEnemyDowned = "onNpcEnemyDowned";
    String onNpcEnemyEnterVehicle = "onNpcEnemyEnterVehicle";
    String onNpcEnemyLeaveVehicle = "onNpcEnemyLeaveVehicle";
    String onNpcEnemyLeftServer = "onNpcEnemyLeftServer";
    String onNpcFollowTargetDied = "onNpcFollowTargetDied";
    String onNpcFollowTargetDowned = "onNpcFollowTargetDowned";
    String onNpcFollowTargetEnterVehicle = "onNpcFollowTargetEnterVehicle";
    String onNpcFollowTargetLeaveVehicle = "onNpcFollowTargetLeaveVehicle";
    String onNpcFollowTargetLeftServer = "onNpcFollowTargetLeftServer";
    String onVehicleCreated = "onVehicleCreated";
    String onVehicleDestroyed = "onVehicleDestroyed";
    String onVehicleExploded = "onVehicleExploded";
    String onVehicleReceiveDamage = "onVehicleReceiveDamage";
    String onObjectCreated = "onObjectCreated";
    String onObjectDestroyed = "onObjectDestroyed";
    String onObjectReceiveDamage = "onObjectReceiveDamage";
    String onObjectTouched = "onObjectTouched";
    String onObjectOverlapped = "onObjectOverlapped";
    String onObjectStoppedOverlapping = "onObjectStoppedOverlapping";
    String onObjectBroken = "onObjectBroken";
    String onZoneCreated = "onZoneCreated";
    String onZoneDestroyed = "onZoneDestroyed";
    String onZoneEnter = "onZoneEnter";
    String onZoneLeave = "onZoneLeave";
}