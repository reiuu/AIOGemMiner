package com.darkan.scripts.impl.aiogems.states;

import com.darkan.api.inter.Interfaces;
import com.darkan.api.inter.chat.Message;
import com.darkan.api.listeners.MessageListener;
import com.darkan.scripts.State;
import com.darkan.scripts.StateMachineScript;


public class Fill extends State implements MessageListener {

    private boolean gemBagFilled = false;

    @Override
    public State checkNext(){
        if (Interfaces.getInventory().freeSlots() == 26)
            return new MineGem();
        if (gemBagFilled)
            return new Deposit();
        return null;
    }

    @Override
    public void loop(StateMachineScript ctx){
        if (Interfaces.getInventory().containsAnyReg("Uncut ") && Interfaces.getInventory().isFull())
            Interfaces.getInventory().clickItemReg("Gem bag", "Fill");
            ctx.sleep(700);
        ctx.setState("Filling gem bag...");
    }

    @Override
    public void onMessageReceived(Message message) {
        if (message.isGame() && message.getText().contains("You have reached") || (message.isGame() && message.getText().contains("You can't store")))
            gemBagFilled = true;
    }
}
