package me.piggyster.api.service;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public final class Service {

    @Nonnull
    public static <T> T load(@Nonnull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return get(clazz).orElseThrow(() -> new IllegalStateException("No registration present for service '" + clazz.getName() + "'"));
    }

    @Nonnull
    public static <T extends PluginService> T grab(@Nonnull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return temp(clazz).orElseThrow(() -> new IllegalStateException("No registration present for service '" + clazz.getName() + "'"));
    }

    @Nonnull
    public static <T extends PluginService> Optional<T> temp(@Nonnull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        RegisteredServiceProvider<T> registration = Bukkit.getServicesManager().getRegistration(clazz);
        return registration == null ? Optional.empty() : Optional.ofNullable(registration.getProvider());
    }

    public static <T> Optional<T> get(@Nonnull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        RegisteredServiceProvider<T> registration = Bukkit.getServicesManager().getRegistration(clazz);
        return registration == null ? Optional.empty() : Optional.ofNullable(registration.getProvider());
    }

    @Nonnull
    public static <T> T provide(@Nonnull Class<T> clazz, @Nonnull T instance, @Nonnull Plugin plugin, @Nonnull ServicePriority priority) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(instance, "instance");
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(priority, "priority");
        Bukkit.getServicesManager().register(clazz, instance, plugin, priority);
        return instance;
    }

    @Nonnull
    public static <T> T provide(@Nonnull Class<T> clazz, @Nonnull T instance, @Nonnull Plugin plugin) {
        return provide(clazz, instance, plugin, ServicePriority.Normal);
    }

    @Nonnull
    public static <T extends PluginService> T provide(@Nonnull Class<T> clazz, @Nonnull T instance) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(instance, "instance");
        instance.initialize();
        Bukkit.getServicesManager().register(clazz, instance, instance.getPlugin(), ServicePriority.Normal);
        return instance;
    }
}
