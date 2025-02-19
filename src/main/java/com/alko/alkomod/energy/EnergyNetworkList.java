package com.alko.alkomod.energy;

import java.util.HashMap;
import java.util.HashSet;

public class EnergyNetworkList {

    private static HashMap<Integer ,EnergyNetwork> networks = new HashMap<>();

    public EnergyNetworkList(){

    }

    public static void addNetwork(EnergyNetwork network){
        int id = 0;
        while (networks.containsKey(id)) {
            id++;
        }
        networks.put(id, network);
    }

    public static EnergyNetwork getNetworkById(int id){
        return networks.getOrDefault(id, null);
    }

    public static int getFreeId(){
        int id = 0;
        if(networks.isEmpty()) return 0;
        while (networks.containsKey(id)) {
            id++;
        }
        return id;
    }
}
