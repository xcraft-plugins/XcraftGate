package de.xcraft.voronwe.xcraftgate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GateSave {
   List<DataGate> gates;

   public List<DataGate> getGates() {
      return this.gates;
   }

   public void setGates(List<DataGate> gates) {
      this.gates = gates;
   }

   public GateSave(Collection<DataGate> values) {
      this.gates = new ArrayList(values);
   }
}
