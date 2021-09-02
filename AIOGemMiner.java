package com.darkan.scripts.impl.aiogems;

import com.darkan.scripts.Script;
import com.darkan.scripts.State;
import com.darkan.scripts.StateMachineScript;
import com.darkan.scripts.impl.aiogems.states.MineGem;

@Script(value = "AIO Gem Miner")
public class AIOGemMiner extends StateMachineScript {



    public AIOGemMiner() {
        super("AIO Gem Miner");
    }



    @Override
    public boolean onStart() {return true;}

    @Override
    public State getStartState() {
        return new MineGem();
    }

    @Override
    public void paintImGui(long runtime) {
        printGenericXpGain(runtime);
    }

    @Override
    public void paintOverlay(long runtime) {

    }

    @Override
    public void onStop() {

    }
}