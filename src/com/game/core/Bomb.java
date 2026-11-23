package com.game.core;

import com.game.modules.GameModule;
import java.util.*;

public class Bomb {
    
    private final String serial_number;
    private final int battery_AA;
    private final int battery_D;
    private final List<String> ports;
    private final Map<String, Boolean> indicators;
    private final List<GameModule> modules;

    private int strike = 0;
    private final int max_strikes = 3;
    private Runnable onBombExplodedCallBack;

    public Bomb() {
        this.serial_number = generateSerialNumber();
        this.ports = new ArrayList<>();
        this.indicators = new HashMap<>();
        this.modules = new ArrayList<>();

        Random r = new Random();
        this.battery_AA = r.nextInt(3);
        this.battery_D = r.nextInt(2);

        generateIndicators(r);
        generatePorts(r);
    }

    private String generateSerialNumber() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        sb.append(r.nextInt(10));

        return sb.toString();
    }

    private void generatePorts(Random r) {
        String[] port_list = {"DVI-D", "Parallel", "PS/2", "RJ-45", "Serial","Stereo RCA"};
        for (String port : port_list) {
            if (r.nextDouble() < 0.3) {
                ports.add(port);
            }
        }
    }

    private void generateIndicators(Random r) {
        String[] labels = {"SND", "CLR", "CAR", "IND", "FRQ", "SIG", "NSA", "MSA", "TRN", "BOB", "FRK"};
        List<String> label_list = new ArrayList<>(Arrays.asList(labels));
        Collections.shuffle(label_list);
        int count = 2 + r.nextInt(2);
        for (int i = 0; i < count; i++) {
            indicators.put(label_list.get(i), r.nextBoolean());
        }
    }

    public boolean isLastDigitOdd() {
        char lastChar = serial_number.charAt(serial_number.length() - 1);
        return Character.isDigit(lastChar) && ((lastChar - '0') % 2 != 0);
    }

    public boolean hasVowelInSerial() {
        return serial_number.matches(".*[AEIOU].*");
    }

    public boolean isIndicatorLit(String indicator) {
        return indicators.getOrDefault(indicator, false);
    }

    public boolean hasPort(String port) {
        return ports.contains(port);
    }

    public void addModule(GameModule module) {
        modules.add(module);
    }

    public boolean areAllModulesSolved() {
        return !modules.isEmpty() && modules.stream().allMatch(GameModule::isSolved);
    }

    public void addStrike() {
        strike++;
        if (strike >= max_strikes && onBombExplodedCallBack != null) {
            onBombExplodedCallBack.run();
        }
    }

    public void setOnBombExplodedCallback(Runnable callback) {
        this.onBombExplodedCallBack = callback;
    }

    public List<GameModule> getModules() { return Collections.unmodifiableList(modules); }
    public String getSerialNumber() { return serial_number; }
    public int getBatteryAA() { return battery_AA; }
    public int getBatteryD() { return battery_D; }
    public int getTotalBatteries() { return battery_AA + battery_D; }
    public Map<String, Boolean> getIndicators() { return Collections.unmodifiableMap(indicators); }
    public List<String> getPorts() { return Collections.unmodifiableList(ports); }
    public int getStrikes() { return strike; }
    public int getMaxStrikes() { return max_strikes; }
}
