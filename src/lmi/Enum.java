//  package lmi;
//  
//  public enum Enum {
//      public static enum Command {
//          String ACTION("act"),
//  
//          public static enum Action {
//              DIG("dig"),
//              MINE("mine"),
//              CARRY("carry"),
//              DESTROY("destroy"),
//              FISH("fish"),
//              INSPECT("inspect"),
//              REPAIR("repair"),
//              CRIME("crime"),
//              SWIM("swim"),
//              TRACKING("tracking"),
//              AGGRO("aggro"),
//              SHOOT("shoot");
//  
//              private String action_;
//              public Action(String action) { action_ = action; }
//              public action() { return action_; }
//          }
//  
//          public static final String C_CLICK = "click";
//  
//          private String command_;
//          public Command(String command) { command_ = command; }
//          public command() { return command_; }
//      }
//  
//      public static enum ResourceName {
//          public static class Gauge {
//              HIT_POINT("gfx/hud/meter/hp"),
//              STAMINA("gfx/hud/meter/stam"),
//              ENERGY("gfx/hud/meter/nrj");
//  
//              private String resourceName_;
//              public Gauge(String resourceName) { resourceName_ = resourceName; }
//              public resourceName { return resourceName_; }
//          }
//      }
//  }
