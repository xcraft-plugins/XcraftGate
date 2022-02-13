package de.xcraft.voronwe.xcraftgate;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginManager implements Runnable {
   private final XcraftGate core;
   private final org.bukkit.plugin.PluginManager pm;
   private Plugin vault = null;
   private Permission permission = null;
   private Economy economy = null;

   public PluginManager(XcraftGate core) {
      this.core = core;
      this.pm = core.getServer().getPluginManager();
   }

   public void registerEvents(Listener listener) {
      this.pm.registerEvents(listener, this.core);
   }

   public Permission getPermissions() {
      return this.permission;
   }

   public Economy getEconomy() {
      return this.economy;
   }

   private void checkPluginVault() {
      if (this.vault == null) {
         Plugin vaultCheck = this.pm.getPlugin("Vault");
         if (vaultCheck != null && vaultCheck.isEnabled()) {
            this.vault = vaultCheck;
            this.core.logInfo("found Vault plugin.");
            RegisteredServiceProvider<Permission> permissionProvider = this.core.getServer().getServicesManager().getRegistration(Permission.class);
            if (permissionProvider != null) {
               this.permission = (Permission)permissionProvider.getProvider();
               this.core.logInfo("Reported permission provider: " + this.permission.getName());
            }

            RegisteredServiceProvider<Economy> economyProvider = this.core.getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
               this.economy = (Economy)economyProvider.getProvider();
               this.core.logInfo("Reported economy provider: " + this.economy.getName());
            }
         }

      }
   }

   public void checkDisabledPlugin(Plugin plugin) {
      if (plugin.getDescription().getName().equals("Vault")) {
         this.permission = null;
         this.economy = null;
         this.vault = null;
         this.core.logInfo("lost Vault plugin");
      }

   }

   public void run() {
      this.checkPluginVault();
   }
}
