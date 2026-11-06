package zfuctoneT.integration;

import zfuctoneT.ZFuctone;
import zfuctoneT.integration.flectonepulse.FlectonePulseModule;
import zfuctoneT.integration.floodgate.FloodgateModule;
import zfuctoneT.integration.miniplaceholders.MiniPlaceholdersModule;
import zfuctoneT.integration.placeholderapi.PlaceholderAPIModule;
import zfuctoneT.integration.viaversion.ViaVersionModule;
import zfuctoneT.model.Module;
import zfuctoneT.model.config.IntegrationConfig;
import zfuctoneT.model.constant.IntegrationConstant;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class Integration implements Module<Integration> {
    PlaceholderAPI placeholderAPI = new PlaceholderAPI();
    MiniPlaceholders miniPlaceholders = new MiniPlaceholders();
    ViaVersion viaVersion = new ViaVersion();
    FlectonePulse flectonePulse = new FlectonePulse();
    Geyser geyser = new Geyser();
    Floodgate floodgate = new Floodgate();
    Luckperms luckperms = new Luckperms();

    @Override
    public Integration enable() {
        if (isDisable()) return this;
        placeholderAPI.enable();
        miniPlaceholders.enable();
        return this;
    }

    @Override
    public Integration disable() {
        placeholderAPI.disable();
        miniPlaceholders.disable();
        return this;
    }

    @Override
    public Integration reload() {
        if (isDisable()) return disable();
        placeholderAPI.reload();
        miniPlaceholders.reload();
        return this;
    }

    private boolean isDisable() {
        return !ZFuctone.get(IntegrationConfig.class).enable();
    }

    private static abstract class IntegrationModule<T> implements Module<T> {
        protected final static IntegrationConfig integrationConfig = ZFuctone.get(IntegrationConfig.class);

        protected boolean isEnable(boolean enable) {
            return enable && isPluginExits();
        }

        public abstract boolean isEnable();

        public abstract String getName();

        public boolean isPluginExits() {
            return Bukkit.getPluginManager().getPlugin(getName()) != null;
        }
    }

    public static class PlaceholderAPI extends IntegrationModule<PlaceholderAPI> {
        @Override
        public boolean isEnable() {
            return isEnable(integrationConfig.placeholderapi().enable());
        }

        @Override
        public String getName() {
            return IntegrationConstant.PlaceholderAPI.NAME;
        }

        @Override
        public PlaceholderAPI enable() {
            if (isEnable()) PlaceholderAPIModule.enable();
            return this;
        }

        @Override
        public PlaceholderAPI disable() {
            if (isPluginExits()) PlaceholderAPIModule.disable();
            return this;
        }

        @Override
        public PlaceholderAPI reload() {
            return disable().enable();
        }

        public String setPlaceholders(Player player, String message) {
            if (!isEnable()) return message;
            return PlaceholderAPIModule.setPlaceholders(player, message);
        }

        public String setPlaceholders(OfflinePlayer player, String message) {
            if (!isEnable()) return message;
            return PlaceholderAPIModule.setPlaceholders(player, message);
        }

        public String setPlaceholders(String message) {
            if (!isEnable()) return message;
            return PlaceholderAPIModule.setPlaceholders(null, message);
        }

        public String setPlaceholders(Object object, String message) {
            if (!isEnable()) return message;
            return switch (object) {
                case Player player -> setPlaceholders(player, message);
                case OfflinePlayer offlinePlayer -> setPlaceholders(offlinePlayer, message);
                case CommandSender sender -> setPlaceholders(sender, message);
                default -> setPlaceholders(message);
            };
        }
    }

    public static class MiniPlaceholders extends IntegrationModule<MiniPlaceholders> {
        @Override
        public boolean isEnable() {
            return isEnable(integrationConfig.miniplaceholders().enable());
        }

        @Override
        public String getName() {
            return IntegrationConstant.MiniPlaceholders.NAME;
        }

        @Override
        public MiniPlaceholders enable() {
            return this;
        }

        @Override
        public MiniPlaceholders disable() {
            return this;
        }

        @Override
        public MiniPlaceholders reload() {
            return this;
        }

        public TagResolver getAudiencePlaceholders() {
            if (!isEnable()) return TagResolver.empty();
            return MiniPlaceholdersModule.getAudiencePlaceholders();
        }

        public TagResolver getGlobalPlaceholders() {
            if (!isEnable()) return TagResolver.empty();
            return MiniPlaceholdersModule.getGlobalPlaceholders();
        }

        public TagResolver getAudienceGlobalPlaceholders() {
            if (!isEnable()) return TagResolver.empty();
            return MiniPlaceholdersModule.getAudienceGlobalPlaceholders();
        }
    }

    public static class ViaVersion extends IntegrationModule<ViaVersion> {
        @Override
        public boolean isEnable() {
            return isEnable(integrationConfig.viaversion().enable());
        }

        @Override
        public String getName() {
            return IntegrationConstant.ViaVersion.NAME;
        }

        @Override
        public ViaVersion enable() {
            return this;
        }

        @Override
        public ViaVersion disable() {
            return this;
        }

        @Override
        public ViaVersion reload() {
            return this;
        }

        public String getPlayerVersion(UUID uuid) {
            if (!isEnable()) return null;
            return ViaVersionModule.getPlayerVersion(uuid);
        }
    }

    public static class FlectonePulse extends IntegrationModule<FlectonePulse> {
        @Override
        public boolean isEnable() {
            return isEnable(integrationConfig.flectonepulse().enable());
        }

        @Override
        public String getName() {
            return IntegrationConstant.FlectonePulse.NAME;
        }

        @Override
        public FlectonePulse enable() {
            if (isEnable()) FlectonePulseModule.enable();
            return this;
        }

        @Override
        public FlectonePulse disable() {
            if (isPluginExits()) FlectonePulseModule.disable();
            return this;
        }

        @Override
        public FlectonePulse reload() {
            return disable().enable();
        }
    }

    public static class Geyser {

    }

    public static class Floodgate extends IntegrationModule<Floodgate> {
        @Override
        public boolean isEnable() {
            return isEnable(integrationConfig.floodgate().enable());
        }

        @Override
        public String getName() {
            return IntegrationConstant.Floodgate.NAME;
        }

        @Override
        public Floodgate enable() {
            return this;
        }

        @Override
        public Floodgate disable() {
            return this;
        }

        @Override
        public Floodgate reload() {
            return this;
        }

        public boolean isBedrockPlayer(UUID uuid) {
            if (!isEnable()) return false;
            return FloodgateModule.isBedrockPlayer(uuid);
        }

        public String getBedrockPlayerVersion(UUID uuid) {
            if (!isEnable()) return null;
            return FloodgateModule.getBedrockPlayerVersion(uuid);
        }
    }

    public static class Luckperms {

    }
}
