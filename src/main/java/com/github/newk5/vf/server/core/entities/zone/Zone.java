package com.github.newk5.vf.server.core.entities.zone;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.*;
import com.github.newk5.vf.server.core.utils.Log;

public class Zone extends GameEntity {

    private Zone(int id) {
        super();
        this.id = id;
        this.type = GameEntityType.ZONE;
    }

    private native boolean nativeIsCircleShaped(int id);

    public boolean isCircleShaped() {
        if (threadIsValid()) {
            return nativeIsCircleShaped(id);
        }
        return false;
    }

    private native void nativeSetCircleShape(int id, boolean Status);

    public Zone setCircleShape(boolean status) {
        if (isOnMainThread()) {
            nativeSetCircleShape(id, status);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetCircleShape(id, status);
            });

        }
        return this;
    }

    private native int nativeAttachToEntity(int id, int type, int entityId, String socketName);

    public AttachResult attachToEntity(GameEntity entity, String socketName) {
        if (threadIsValid()) {
            int r = nativeAttachToEntity(id, entity.type.value, entity.getId(), socketName);
            AttachResult result = AttachResult.value(r);
            if (result.getCode() > 0) {
                switch (result) {
                    case ERROR_FAILEDTOFINDSOCKET:
                        Log.error(result.getDescription(), socketName);
                        break;
                    case ERROR_FAILEDTOFINDSOURCEENTITY:
                        Log.error(result.getDescription(), toString());
                        break;
                    case ERROR_FAILEDTOFINDTARGETENTITY:
                        Log.error(result.getDescription(), entity.toString());
                        break;
                    case ERROR_INVALIDENTITYTYPE:
                        Log.error(result.getDescription(), entity.type.value + "");
                        break;
                    case ERROR_CANNOTATTACH:
                        Log.error(result.getDescription(), toString());
                        break;
                    case ERROR_ALREADYATTACHED:
                        Log.error(result.getDescription(), getAttachedEntity().toString());
                        break;
                    default:
                        break;
                }
            }
            return result;
        }
        return null;
    }

    private native GameEntity nativeGetAttachedEntity(int id);

    public GameEntity getAttachedEntity() {
        if (threadIsValid()) {
            return nativeGetAttachedEntity(id);
        }
        return null;
    }

    private native boolean nativeIsAttached(int id);

    public boolean isAttached() {
        if (threadIsValid()) {
            return nativeIsAttached(id);
        }
        return false;
    }

    public boolean isAttachedTo(GameEntity e) {
        if (threadIsValid()) {
            return (nativeIsAttached(id) && nativeGetAttachedEntity(id).equals(e));
        }
        return false;
    }

    private native void nativeDetach(int id);

    public Zone detach() {
        if (isOnMainThread()) {
            nativeDetach(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetach(id);
            });
        }
        return this;
    }

    private native boolean nativeIsPointInsideSphere(int id, double x, double y, double z);

    public boolean isPointInsideSphere(double x, double y, double z) {
        if (threadIsValid()) {
            return nativeIsPointInsideSphere(id, x, y, z);
        }
        return false;
    }

    public double getSphereSize() {
        if (threadIsValid()) {
            return getSize().x;
        }
        return -1;
    }

    public Zone setSphereSize(double size) {
        if (isOnMainThread()) {

            nativeSetSize(id, size, 0);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSize(id, size, 0);
            });
        }
        return this;
    }
    
    public Zone setCircleSize(double size){
        setSphereSize(size);
        return this;
    }
    
      public double getCircleSize() {
        if (threadIsValid()) {
            return getSize().x;
        }
        return -1;
    }

    public boolean isPointInsideSphere(Vector v) {
        if (threadIsValid()) {
            return nativeIsPointInsideSphere(id, v.x, v.y, v.z);
        }
        return false;
    }

    private native int nativeGetStyle(int id);

    public int getStyle() {
        return threadIsValid() ? this.nativeGetStyle(this.id) : -1;
    }

    private native boolean nativeOverlapEventsEnabled(int id);

    public boolean hasOverlapEventsEnabled() {
        if (threadIsValid()) {
            return nativeOverlapEventsEnabled(id);
        }
        return false;
    }

    private native void nativeSetOverlapEventsEnabled(int id, boolean Status);

    public Zone setOverlapEventsEnabled(boolean Status) {
        if (isOnMainThread()) {
            nativeSetOverlapEventsEnabled(id, Status);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetOverlapEventsEnabled(id, Status);
            });

        }
        return this;
    }

    private native void nativeSetStyle(int id, int Style);

    public Zone setStyle(int Style) {
        if (isOnMainThread()) {
            nativeSetStyle(id, Style);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetStyle(id, Style);
            });

        }
        return this;
    }

    private native boolean nativeIsPointInside(int id, double X, double Y);

    public boolean isPointInside(double X, double Y) {
        if (threadIsValid()) {
            return nativeIsPointInside(id, X, Y);
        }
        return false;
    }

    public boolean isEntityInside(GameEntity e) {
        Vector v = e.getPosition();
        return isPointInside(v.x, v.y);
    }

    public boolean isEntityInsideSphere(GameEntity e) {
        Vector v = e.getPosition();
        return isPointInsideSphere(v.x, v.y, v.z);
    }

    private native void nativeSetSize(int id, double SizeX, double SizeY);

    public Zone setSize(double SizeX, double SizeY) {
        if (isOnMainThread()) {
            nativeSetSize(id, SizeX, SizeY);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSize(id, SizeX, SizeY);
            });
        }
        return this;
    }

    public Zone setSize(Vector2D size) {
        if (isOnMainThread()) {
            nativeSetSize(id, size.x, size.y);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSize(id, size.x, size.y);
            });
        }
        return this;
    }

    private native Vector2D nativeGetSize(int id);

    public Vector2D getSize() {
        return threadIsValid() ? this.nativeGetSize(this.id) : null;
    }

    private native void nativeSetTransform(int id, double x, double y, double z, double yaw, double pitch, double roll);

    public Zone setTransform(Transform t) {
        if (isOnMainThread()) {
            nativeSetTransform(id, t.position.x, t.position.y, t.position.z, t.rotation.yaw, t.rotation.pitch, t.rotation.roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTransform(id, t.position.x, t.position.y, t.position.z, t.rotation.yaw, t.rotation.pitch, t.rotation.roll);
            });
        }
        return this;
    }

    private native void nativeSetColor(int id, int Color);

    public Zone setColor(int Color) {
        if (isOnMainThread()) {
            nativeSetColor(id, Color);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetColor(id, Color);
            });

        }
        return this;
    }

    private native void nativeSetHeight(int id, double height);

    public Zone setHeight(double height) {
        if (isOnMainThread()) {
            nativeSetHeight(id, height);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHeight(id, height);
            });

        }
        return this;
    }

    @Override
    public Vector getPosition() {
        if (threadIsValid()) {
            return nativeGetTransform(id).getPosition();
        }
        return null;
    }

    public Zone setPosition(Vector pos) {
        if (isOnMainThread()) {
            Transform t = nativeGetTransform(id);
            t.setPosition(pos);
            setTransform(t);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                Transform t = nativeGetTransform(id);
                t.setPosition(pos);
                setTransform(t);
            });
        }
        return this;
    }

    public double getYawAngle() {
        if (threadIsValid()) {
            return nativeGetTransform(id).getRotation().yaw;
        }
        return 0;
    }

    public Zone setYawAngle(double yaw) {
        if (isOnMainThread()) {
            Rotation r = getRotation();
            r.yaw = yaw;
            setRotation(r);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                Rotation r = getRotation();
                r.yaw = yaw;
                setRotation(r);
            });
        }
        return this;
    }

    public Rotation getRotation() {
        if (threadIsValid()) {
            return nativeGetTransform(id).getRotation();
        }
        return null;
    }

    public Zone setRotation(Rotation rot) {
        if (isOnMainThread()) {
            Transform t = nativeGetTransform(id);
            t.setRotation(rot);
            setTransform(t);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                Transform t = nativeGetTransform(id);
                t.setRotation(rot);
                setTransform(t);
            });
        }
        return this;
    }

    private native Transform nativeGetTransform(int id);

    public Transform getTransform() {
        return threadIsValid() ? this.nativeGetTransform(this.id) : null;
    }

    private native boolean nativeIsVisible(int id);

    public boolean isVisible() {
        if (threadIsValid()) {
            return nativeIsVisible(id);
        }
        return false;
    }

    private native void nativeSetVisible(int id, boolean Status);

    public Zone setVisible(boolean Status) {
        if (isOnMainThread()) {
            nativeSetVisible(id, Status);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetVisible(id, Status);
            });

        }
        return this;
    }

    private native void nativeDestroy(int id);

    public Zone destroy() {
        if (isOnMainThread()) {
            nativeDestroy(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDestroy(id);
            });
        }
        return this;
    }
}
