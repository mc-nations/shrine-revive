package com.itsziroy.shrinerevive.jobs;

import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class SpawnRandomFireworks extends Job{

    Location location;
    public SpawnRandomFireworks(ShrineRevive plugin, Location location) {
        super(plugin);
        this.location = location;
    }

    @Override
    public void run() {
        location.setX(location.getX() + Math.random() * 8 - 4);
        location.setY(location.getY() + Math.random() * 8 - 4);

        World world = location.getWorld();

        if(world != null) {

            Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);

            FireworkMeta meta = firework.getFireworkMeta();

            FireworkEffect.Builder builder = FireworkEffect.builder();

            builder.withFlicker().withColor(Color.fromRGB((int) (Math.random() * 16777215)),
                            Color.fromRGB((int) (Math.random() * 16777215)),
                            Color.fromRGB((int) (Math.random() * 16777215)),
                            Color.fromRGB((int) (Math.random() * 16777215))).
                    withFade(Color.fromRGB((int) (Math.random() * 16777215))).trail(true).
                    with(FireworkEffect.Type.values()[(int) (Math.random() * 4 + 1)]);
            meta.setPower((int) (Math.random() * 2 + 1));
            meta.addEffect(builder.build());

            firework.setFireworkMeta(meta);
        }
    }
}
