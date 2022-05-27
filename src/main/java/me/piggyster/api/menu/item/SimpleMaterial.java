package me.piggyster.api.menu.item;

import org.bukkit.Material;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.Base64;

public class SimpleMaterial implements Serializable {

    private Material material;
    private String skullTexture;

    public SimpleMaterial(Material material) {
        this.material = material;
    }

    public SimpleMaterial(String skullTexture) {
        material = Material.PLAYER_HEAD;
        this.skullTexture = skullTexture;
    }

    public Material getMaterial() {
        return material;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public String deserialize() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            return new String(Base64.getEncoder().encode(baos.toByteArray()));
        } catch(Exception ex) {
            return null;
        }
    }

    public static SimpleMaterial serialize(String byte64) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(byte64));
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (SimpleMaterial) ois.readObject();
        } catch(Exception ex) {
            return null;
        }
    }


}
