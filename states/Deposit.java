
package com.darkan.scripts.impl.aiogems.states;

import com.darkan.api.accessors.WorldObjects;
import com.darkan.api.entity.MyPlayer;
import com.darkan.api.inter.Interfaces;
import com.darkan.api.util.Utils;
import com.darkan.api.world.WorldObject;
import com.darkan.api.world.WorldTile;
import com.darkan.scripts.State;
import com.darkan.scripts.StateMachineScript;
import com.darkan.api.util.Area;
import kraken.plugin.api.Move;
import kraken.plugin.api.Vector2i;
import kraken.plugin.api.Bank;
import kraken.plugin.api.Actions;

public class Deposit extends State{

    private WorldObject depositObj = getClosestBank();

    private static final WorldTile ARCH_GUILD_START = new WorldTile(3336, 3378, 0);
    private static final WorldTile BANK_CHEST = new WorldTile(3362, 3357, 0);
    private static final Area ARCH_GUILD_AREA = new Area(3283, 3380, 3370, 3320);
    private static final Area RETURNED_AREA = new Area(3278, 3342, 3297, 3330);



    @Override
    public State checkNext(){
        if (Interfaces.getInventory().freeSlots() == 26 &&/*&& Utils.getDistanceTo(MyPlayer.getPosition(), RETURNED) == 0*/RETURNED_AREA.inside(MyPlayer.getPosition()))
            return new MineGem();
        return null;
    }

    @Override
    public void loop(StateMachineScript ctx){

        if (!(ARCH_GUILD_AREA.inside(MyPlayer.getPosition()))) {
            ctx.setState("Teleporting to arch guild...");
            Interfaces.getInventory().clickItemReg("Archaeology", "Teleport");
            ctx.sleepWhile(4000, 10000, () -> MyPlayer.get().isAnimationPlaying());
        }


        if (Utils.getDistanceTo(MyPlayer.getPosition(), ARCH_GUILD_START) == 0) {
            ctx.setState("Moving to bank...");
            Move.to(new Vector2i(3362 + (Utils.gaussian(3, 5)), 3357 + Utils.gaussian(4, 2)));
            ctx.sleepWhile(2000, 20000, () -> MyPlayer.get().isAnimationPlaying());
        }

        if ((Utils.getDistanceTo(MyPlayer.getPosition(), BANK_CHEST) <= 5) && !(Bank.isOpen())){
            if (depositObj == null){
                depositObj = getClosestBank();
            }
            ctx.setState("Using bank...");
            depositObj.interact("Use");
            ctx.sleepWhile(1000, 2000, () -> !(Bank.isOpen()));
        }

        if  (Bank.isOpen() && Interfaces.getInventory().containsAnyReg("Uncut ")){
            ctx.setState("Depositing gems...");
            Interfaces.getBankInventory().clickItemReg("Uncut ", "Deposit-All");
            ctx.sleep(1000);
            ctx.setState("Emptying bag...");
            Actions.menu(14, 8, 0, 33882127, 1);
            ctx.sleep(1200);
        }

        if (Interfaces.getInventory().freeSlots() == 26){
            ctx.setState("Running back to gem rocks... (1)");
            Move.to(new Vector2i(3289 + Utils.gaussian(3, 2), 3335 + Utils.gaussian(3, 2)));
            ctx.sleepWhile(4000, 20000, () -> MyPlayer.get().isAnimationPlaying());
        }
    }

    public WorldObject getClosestBank(){
        return WorldObjects.getClosest(object -> object.hasOption("Use") && object.getName().equals("Bank chest"));
    }

}
