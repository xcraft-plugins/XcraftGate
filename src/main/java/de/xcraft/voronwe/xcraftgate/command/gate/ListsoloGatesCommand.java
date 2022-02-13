package de.xcraft.voronwe.xcraftgate.command.gate;

import de.xcraft.voronwe.xcraftgate.DataGate;
import de.xcraft.voronwe.xcraftgate.XcraftGate;
import de.xcraft.voronwe.xcraftgate.command.GateSubcommand;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ListsoloGatesCommand extends GateSubcommand {
   public ListsoloGatesCommand(XcraftGate plugin) {
      super(plugin);
   }

   public void execute(String gateName, List<String> args) {
      Iterator var3 = this.plugin.getGates().iterator();

      while(true) {
         DataGate thisGate;
         do {
            if (!var3.hasNext()) {
               return;
            }

            thisGate = (DataGate)var3.next();
         } while(thisGate.hasTarget());

         boolean hasSource = false;
         Iterator var6 = this.plugin.getGates().iterator();

         while(var6.hasNext()) {
            DataGate sourceGate = (DataGate)var6.next();
            if (sourceGate.getTarget() != null && sourceGate.getTarget().equals(thisGate)) {
               hasSource = true;
               break;
            }
         }

         if (!hasSource) {
            this.reply("Found orphan: " + thisGate.getName());
         }
      }
   }

   public List<String> tabComplete(List<String> args) {
      return Collections.emptyList();
   }
}
