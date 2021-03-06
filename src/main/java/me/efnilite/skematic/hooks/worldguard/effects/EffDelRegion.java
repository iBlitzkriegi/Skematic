package me.efnilite.skematic.hooks.worldguard.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.efnilite.skematic.hooks.worldguard.WorldGuard;
import me.efnilite.skematic.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

@Name("Region - Delete")
@Description("Delete a region.")
@Examples("delete region \"newregion\" in \"world\"")
@Since("1.0.0")
public class EffDelRegion extends Effect {

    static {
        Skript.registerEffect(EffDelRegion.class, "del[ete] [the] [worldguard] region [(named|call)] %string% in %world%");
    }

    private Expression<String> name;
    private Expression<World> world;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {

        name = (Expression<String>) exprs[0];
        world = (Expression<World>) exprs[1];

        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "delete region " + name.toString(e, debug) + " in world " + world.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        RegionManager regionManager = WorldGuard.getWorldGuard().getRegionManager(Bukkit.getServer().getWorld(world.getSingle(e).toString()));
        if (regionManager != null) {
            TaskManager.manager.async(() ->
                    regionManager.removeRegion(name.getSingle(e))
            );
        }
    }
}
