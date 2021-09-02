package com.darkan.scripts.impl.aiogems.states;

import com.darkan.api.inter.Interfaces;
import com.darkan.api.accessors.WorldObjects;
import com.darkan.api.util.Utils;
import com.darkan.scripts.State;
import com.darkan.scripts.StateMachineScript;
import com.darkan.api.entity.MyPlayer;


public class MineGem extends State {

    private String getGemRock(){return "Uncommon gem rock";}

    @Override
    public State checkNext(){
        if (Interfaces.getInventory().isFull() && Interfaces.getInventory().containsAnyReg("Uncut "))
            return new Fill();
        return null;
    }

    @Override public void loop(StateMachineScript ctx){
        if (WorldObjects.interactClosestReachable("Mine", object -> object.getName().equals(getGemRock()) && object.hasOption("Mine"))) {
            ctx.sleepWhile(3000, Utils.gaussian(9000, 8000), () -> MyPlayer.get().isAnimationPlaying());
        }
        ctx.setState("Mining gems...");
    }

}
