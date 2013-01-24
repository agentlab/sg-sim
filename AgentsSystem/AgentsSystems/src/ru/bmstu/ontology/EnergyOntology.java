/********************************************************************************
* BankOntology:
* -------------
* This class is an example showing how to use JADE content language support  ,
* to define an application ontology.
*
* Version 1.0 - July 2003
* Author: Ambroise Ncho, under the supervision of Professeur Jean Vaucher.
*
* Universite de Montreal - DIRO
*
*********************************************************************************/

package ru.bmstu.ontology;

import jade.content.onto.*;
import jade.content.schema.*;


public class EnergyOntology extends Ontology {

   // ----------> The name identifying this ontology
   public static final String ONTOLOGY_NAME = "Bank-Ontology";

   // ----------> The singleton instance of this ontology
   private static Ontology instance = new EnergyOntology();

   // ----------> Method to access the singleton ontology object
   public static Ontology getInstance() { return instance; }


   // Private constructor
   private EnergyOntology() {

      super(ONTOLOGY_NAME, BasicOntology.getInstance());

      try {

         // CreateAccount
         AgentActionSchema as = new AgentActionSchema("EnergyExchange");
         add(as, Energy.class);
         as.add("needToBuy", (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
         as.add("price", (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
         as.add("quantity", (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);

      }
      catch (OntologyException oe) {
         oe.printStackTrace();
      }
   }
}// BankOntology