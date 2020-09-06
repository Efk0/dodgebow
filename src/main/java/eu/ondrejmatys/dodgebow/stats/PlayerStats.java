package eu.ondrejmatys.dodgebow.stats;

public interface PlayerStats {

    Object getValue(String playerName, String valueName);

    void setValue(String playerName, String valueName, String value);
}
