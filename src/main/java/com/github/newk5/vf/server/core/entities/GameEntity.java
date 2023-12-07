package com.github.newk5.vf.server.core.entities;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.entities.zone.Zone;
import com.github.newk5.vf.server.core.exceptions.InvalidThreadException;
import com.github.newk5.vf.server.core.utils.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.concurrent.ConcurrentHashMap;

public abstract class GameEntity {

    private long threadId;
    public GameEntityType type;
    protected int id;
    private GameData gameData;
    private Map<String, Object> data;
    private List<String> tags;

    public GameEntity() {
        this.threadId = Thread.currentThread().getId();
    }

    public <T> T findClosest(List<? extends GameEntity> entities) {
        return (T) entities.stream().min(Comparator.comparingDouble(ent -> {
            return getPosition().distanceTo(ent.getPosition());
        })).orElse(null);
    }

    public Vector getClosest(List<Vector> positions) {
        return positions.stream().min(Comparator.comparingDouble(pos -> {
            return getPosition().distanceTo(pos);
        })).orElse(null);
    }

    public GameCharacter asGameCharacter() {
        return (GameCharacter) this;
    }

    protected boolean isOnMainThread() {
        return (this.threadId == Thread.currentThread().getId());
    }

    public List<String> getTags() {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags;
    }

    public Vector directionTo(GameEntity ent) {
        return InternalServerEvents.server.findDirectionLookingAt(getPosition(), ent.getPosition());
    }

    public Vector directionTo(Vector location) {
        return InternalServerEvents.server.findDirectionLookingAt(getPosition(), location);
    }

    public GameEntity setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public boolean hasTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        return tags.contains(tag);
    }

    public boolean hasAnyTag(String... tagsVar) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        for (String tag : Arrays.asList(tagsVar)) {
            if (tags.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public GameEntity addTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        return this;
    }

    public GameEntity removeTag(String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.remove(tag);
        return this;
    }

    public boolean isInsideZone(Zone z) {
        if (z == null) {
            return false;
        }
        return z.isEntityInside(this);
    }

    public boolean isInsideSphereZone(Zone z) {
        if (z == null) {
            return false;
        }
        return z.isEntityInsideSphere(this);
    }

    protected boolean threadIsValid() {
        if (this.threadId != Thread.currentThread().getId()) {
            InvalidThreadException e = new InvalidThreadException("You cannot use the server API from outside the main server thread");
            Log.exception(e);
            return false;
        } else {
            return true;
        }
    }

    public boolean isValid() {
        return InternalServerEvents.isValid(this);
    }

    public int getId() {
        return this.id;
    }

    public GameData getGameData() {
        return this.gameData;
    }

    public <T> T getCastedGameData() {
        return (T) this.gameData;
    }

    public GameEntity setGameData(GameData gameData) {
        this.gameData = gameData;
        return this;
    }

    public Map<String, Object> getData() {
        if (this.data == null) {
            this.data = new ConcurrentHashMap<>();
        }
        return this.data;
    }

    public <T> T getData(String key) {
        if (this.data != null) {
            return (T) this.data.get(key);
        }
        return null;
    }

    public GameEntity putData(String key, Object value) {
        if (this.data == null) {
            this.data = new ConcurrentHashMap<>();
        }
        this.data.put(key, value);
        return this;
    }

    public GameEntity removeData(String key) {
        if (this.data != null) {
            this.data.remove(key);
        }
        return this;
    }

    public GameEntity clearData() {
        if (this.data != null) {
            data.clear();
        }
        return this;
    }

    public boolean hasData(String key) {
        if (this.data != null) {
            return this.data.containsKey(key);
        }
        return false;
    }

    public boolean hasNonNullData(String key) {
        return this.getData(key) != null;
    }

    public GameEntity clearTags() {
        if (this.tags != null) {
            this.tags.clear();
        }
        return this;
    }

    public Player asPlayer() {
        return (Player) this;
    }

    public Vehicle asVehicle() {
        return (Vehicle) this;
    }

    public NPC asNpc() {
        return (NPC) this;
    }

    public DamageableEntity asDamageableEntity() {
        return (DamageableEntity) this;
    }

    public Vector getPosition() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getPosition();
        } else if (this instanceof Vehicle) {
            Vehicle p = (Vehicle) this;
            return p.getPosition();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getPosition();
        } else if (this instanceof GameObject) {
            GameObject obj = (GameObject) this;
            return obj.getPosition();
        } else if (this instanceof Zone) {
            Zone obj = (Zone) this;
            return obj.getPosition();
        }
        return null;
    }

    @Override
    public String toString() {
        return "GameEntity{" + "type=" + type + ", id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameEntity other = (GameEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        return this.type == other.type;
    }
}
