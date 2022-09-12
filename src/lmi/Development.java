package lmi;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeMap;

import haven.*;

import lmi.AutomationManager.Automation;
import lmi.automation.*;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.gfx.borka.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.*;

public class Development implements Console.Command {
    // Type Define
    private static class CommandMap extends TreeMap<String, Method> {};

    // Field
    private static CommandMap _commandMap;

    // all methods with default access modifier will count on as executable command
    public static void init() {
        _commandMap = new CommandMap();
        Method methodArray[] = Development.class.getDeclaredMethods();
        for (Method method : methodArray) {
            if (!Util.methodHasModifier(method, Modifier.PUBLIC)
                    && !Util.methodHasModifier(method, Modifier.PRIVATE)) {
                _commandMap.put(method.getName(), method);
            }
        }
    }

    private static Method getCommand(String commandString) {
        return _commandMap.get(commandString);
    }

    private static Set<String> getCommandStringSet() {
        return _commandMap.keySet();
    }

    // Run
	public void run(Console cons, String[] args) throws Exception {
        new Thread(new MainRunnable(args)).start();
    }

    private static class MainRunnable implements Runnable {
        private String[] _args;
        private MainRunnable(String[] args) { _args = args; }

        // Runnable Requirment
        public void run() {
            ObjectShadow.ui().cons.out.println("==============================");
            String commandString = null;
            try {
                _checkArgument();

                commandString = _args[1];
                if (commandString.contentEquals("help"))
                    throw new LMIException(ET_COMMAND_HELP);

                _invokeCommand(commandString);
            } catch (Exception e) {
                if (e instanceof LMIException) {
                    final LMIException lmiException = (LMIException)e;
                    switch (lmiException.type()) {
                        case ET_COMMAND_ERROR:
                            _printError(ObjectShadow.ui().cons.out);
                            break;
                        case ET_COMMAND_HELP:
                            _printHelp(ObjectShadow.ui().cons.out);
                            break;
                        case ET_COMMAND_MATCH:
                            Util.error("[" + (commandString != null ? commandString : "") + "]가 뭔지 모르겠어요");
                            break;
                        default:
                            break;
                    }
                } else {
                    Util.debugPrint(e);
                    e.printStackTrace();
                }
            }
        }

        private void _checkArgument() {
            if (_args.length != 2) throw new LMIException(ET_COMMAND_ERROR);
        }

        private void _invokeCommand(String commandString) throws Exception {
            final Method method = Development.getCommand(commandString);

            if (method == null)
                throw new LMIException(ET_COMMAND_MATCH);
            else
                method.invoke(null);
        }

        // Help
        private static void _printError(PrintWriter writer) {
            Util.message("  [dev Manual]");
            Util.error("usage: dev <command>");
            writer.println(" ");
            Util.message("command list:");
            _printCommandStringList(writer);
        }

        private static void _printHelp(PrintWriter writer) {
            Util.message("  [dev Manual]");
            Util.error("usage: dev <command>");
            writer.println(" ");
            Util.message("command list:");
            _printCommandStringList(writer);
        }

        private static void _printCommandStringList(PrintWriter printWriter) {
            for (String command : Development.getCommandStringSet())
                Util.message("  " + command);
        }
    }

    static void sendMenuGridDryingFrameMessage() {
        WidgetMessageHandler.sendMenuGridDryingFrameMessage();
    }

    static void listConsoleCommand() {
        Util.message("console command:");
        for (String command : Util.consoleCommands())
            Util.message("  " + command);
    }

//      // Wrapping ObjectFinder
//      static void objectInitWithRootWidget() {
//          ObjectFinder.init();
//          ObjectFinder.moveForward(ObjectShadow.rootWidget());
//      }
//  
//      static void objectChange() {
//          wrapObjectFinderFind(Util.MemberType.FIELD, null, true);
//      }
//  
//      static void objectUndo() {
//          if (ObjectFinder.isEmpty()) {
//              System.out.println("there is no previous object");
//              return;
//          }
//  
//          ObjectFinder.moveBackward();
//          Debug.describeField(ObjectFinder.last());
//      }
//  
//      static void objectChangeToReturnValueOfMethod() {
//          wrapObjectFinderFind(Util.MemberType.METHOD, null, true);
//      }
//  
//      static void objectDescribe() {
//          Debug.describeField(ObjectFinder.last());
//      }
//  
//      static void objectDescribeAsIterable() {
//          Object object = ObjectFinder.last();
//          if (!(object instanceof Iterable)) {
//              System.out.println(Debug.convertToDebugDescriptionClassNameHashCode(object) + " is not instance of Iterable");
//              return;
//          }
//  
//          System.out.println("[" + Debug.convertToDebugDescriptionClassNameHashCode(object) + "]");
//          for (Object element : (Iterable)object) {
//              Debug.describeField(element);
//          }
//      }
//  
//      // TODO modified ObjectShadow's fields access modifier to private,
//      //  and now can't use this features for objectInit()
//      private static void wrapObjectFinderFind(Util.MemberType type, Class classObjectToReset, boolean willAppend) {
//          Debug.describeClassNameHashCodeWithTag("current: ", ObjectFinder.last());
//  
//          Object object = null;
//          try {
//              object = ObjectFinder.find(type, classObjectToReset);
//              Debug.describeField(object);
//              if (willAppend) {
//                  if (!type.isField())
//                      ObjectFinder.init();
//                  ObjectFinder.moveForward(object);
//              }
//          } catch (Exception e) { e.printStackTrace(); }
//      }
//  
//      static void objectListAsWidget() {
//          ObjectFinder.listLastAsWidget();
//      }

    // Debug
    static void toggleDebugIsPrint() {
        Debug.toggleIsPrint();
    }

    // test command
    static void describeSelf() {
        System.out.println("resource name: " + Self.gob().resourceName());
        System.out.println("Self.location(): " + Self.location());
        System.out.println("Self.hardHitPoint(): " + Self.hardHitPoint());
        System.out.println("Self.softHitPoint(): " + Self.softHitPoint());
        System.out.println("Self.stamina(): " + Self.stamina());
        System.out.println("Self.energy(): " + Self.energy());
    }

    static void describeSelfAttribute() {
        java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> map = Self.gob().attributeMap();
        map.forEach((unused, value) -> {
                Debug.describeField(value);
                });
    }

    // Simple Move
    static void h() {
        Self.moveWest();
    }

    static void j() {
        Self.moveSouth();
    }

    static void jIterate() {
        while (true) {
            final Coord destination = Self.location().add(0, 1);
            Self.move(destination);
            System.out.println("current location: " + Self.location());
        }
    }

    static void k() {
        Self.moveNorth();
    }

    static void l() {
        Self.moveEast();
    }

//      static void describeAllGob() {
//          int count = 1;
//          Array<Gob> gobArray = GobManager.gobArray();
//          for (Gob gob : gobArray) {
//              System.out.println("[" + count + "] { virtual: " + gob.virtual + ", class: " + gob.getClass() + ", resource name: " + gob.resourceName() + " }");
//              ++count;
//          }
//      }

    static void moveCenter() {
        Self.moveCenter();
    }

    static void describeCursorGItem() {
        haven.Widget gItem = WidgetManager.cursorGItem();
        Debug.describeField(gItem);
    }

    static void describeClickedGob() {
        Util.alert("정보를 출력할 물체를 클릭해 주세요");
        Gob gob = ClickManager.getGob();
        Util.message(gob.debugDescription());
    }

//      static void describeClosestGobOverlay() {
//          Gob closestGob = Util.closestGob();
//          System.out.println("[closest gob] " + closestGob.resourceName());
//          for (Gob.Overlay overlay : closestGob.ols) {
//              try {
//                  System.out.println(overlay.res.get().name);
//                  for (byte b : overlay.sdt.rbuf)
//                      System.out.print(" " + b);
//              } catch (NullPointerException e) {
//                  System.out.println("[describeClosestGobOverlay() null pointer exception has occured]");
//              }
//          }
//      }

//      static void describeClosestGobAttribute() {
//          Gob closestGob = Util.closestGob();
//          java.util.Map<Class<? extends haven.GAttrib>, haven.GAttrib> attributeMap = closestGob.attributeMap();
//          String resourceName = closestGob.resourceName();
//          System.out.println("[resource name] " + resourceName);
//          for (haven.GAttrib attribute : attributeMap.values()) {
//              if (attribute instanceof haven.GobIcon
//                      || attribute instanceof haven.Drawable
//                      || attribute instanceof haven.KinInfo
//                      || attribute instanceof haven.GobHealth) {
//                  if (attribute instanceof haven.ResDrawable) {
//                      Debug.describeField(attribute);
//                  } else {
//                      Debug.describeClassNameHashCodeWithTag("[attribute] ", attribute);
//                  }
//              } else {
//                  Debug.describeField(attribute);
//              }
//          }
//      }

//      static void describeClosestGobSdt() {
//          Gob closestGob = Util.closestGob();
//          final haven.Resource resource = closestGob.resource();
//          if(resource == null) {
//              System.out.println("[resource is null]");
//              return;
//          }
//          haven.ResDrawable resourceDrawable = (haven.ResDrawable)closestGob.attribute(haven.ResDrawable.class);
//          byte[] buffer = haven.LMI.resourceDrawableBuffer(resourceDrawable);
//          if (buffer == null) {
//              System.out.println("[buffer is null]");
//              return;
//          }
//          System.out.print("[buffer] length: " + buffer.length);
//          for (byte b : buffer)
//              System.out.print(" " + b);
//          System.out.println();
//      }

//      static void gatherClosestGob() {
//          String action = lmi.Scanner.nextLineWithPrompt("enter action");
//          Gob closestGob = Util.closestGob();
//          FlowerMenuHandler.choose(closestGob, MI_DEFAULT, action);
//          Self.moveNorth();
//      }

    static void investigateGobBoundingBoxWidth() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        Gob variantGob = ClickManager.getGob();

        Coord standardPoint = Self.location();
        Self.lift(standardGob);
        Self.move(standardPoint.add(0, 2048));
        Self.put(standardPoint);

        final int start = TILE_IN_COORD / 8 * 3 + 1;
        int variant = start;
        while (true) {
            final Coord variantPoint = standardPoint.add(variant, 0);
            try {
                _carryWidth(variantGob, variantPoint);
            } catch (LMIException e) {
                break;
            }
            System.out.println("succeeded coord: " + variantPoint);
            --variant;
        }

        System.out.println("failed variant is " + variant);
    }

    private static void _carryWidth(Gob gob, Coord putPoint) {
        Self.lift(gob);
        Self.move(putPoint.add(0, TILE_IN_COORD * 2));
        Self.put(putPoint);
    }

    static void investigateGobBoundingBoxHeight() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        Gob variantGob = ClickManager.getGob();

        Coord standardPoint = Self.location().tileCenter();
        Self.lift(standardGob);
        Self.move(standardPoint.add(0, 2048));
        Self.put(standardPoint);

        final int start = 1024 / 8 * 15;
        int variant = start;
        while (true) {
            final Coord variantPoint = standardPoint.add(0, variant);
            try {
                _carryHeight(variantGob, variantPoint);
            } catch (LMIException e) {
                break;
            }
            System.out.println("succeeded coord: " + variantPoint);
            --variant;
        }

        System.out.println("failed variant is " + variant);

    }

    private static void _carryHeight(Gob gob, Coord putPoint) {
        Self.lift(gob);
        Self.put(putPoint);
    }

//      static void investigateBodyBoundingBoxWidthOnce() {
//          System.out.println("click gob of standard!");
//          Gob standardGob = ClickManager.getGob();
//  
//          String variantString = lmi.Scanner.nextLineWithPrompt("enter variant");
//          final int variant = Util.stoi(variantString);
//  
//          _checkSelfVariantWidth(standardGob, variant);
//  
//      }
//  
//      static void investigateBodyBoundingBoxWidth() {
//          System.out.println("click gob of standard!");
//          Gob standardGob = ClickManager.getGob();
//  
//          final int start = 512;
//          int variant = start;
//          while (true) {
//              try {
//                  _checkSelfVariantWidth(standardGob, variant);
//              } catch (LMIException e) {
//                  break;
//              }
//              variant -= 1;
//          }
//  
//          System.out.println("failed variant is " + variant);
//  
//      }

    private static void _checkSelfVariantWidth(Gob standardGob, int variant) {
        final Coord variantPoint = standardGob.location().add(variant, 0);
        Coord firstStep = variantPoint.north();

        Self.move(firstStep);
        Self.move(variantPoint);

        System.out.println("succeeded coord: " + variantPoint
                + ", distance: " + Self.distance(standardGob));
    }

    static void putLogDistanceBodyWidth() {
        final Coord centerPosition = Self.location().tileCenter();

        System.out.println("첫 번째 로그를 선택해주세요");
        Gob firstLog = ClickManager.getGob();

        System.out.println("두 번째 로그를 선택해주세요");
        Gob secondLog = ClickManager.getGob();

        Self.lift(firstLog);
        Self.move(centerPosition.add(- BW_LOG / 2 - BW_BODY / 2, (BH_LOG + BH_BODY) / 2));
        Self.put(centerPosition.add(- BW_LOG / 2 - BW_BODY / 2, 0));

        Self.lift(secondLog);
        Self.move(centerPosition.add(BW_LOG / 2 + BW_BODY / 2, (BH_LOG + BH_BODY) / 2));
        Self.put(centerPosition.add(BW_LOG / 2 + BW_BODY / 2, 0));

    }

    static void getArea() {
        final Rect area = ClickManager.getArea();
        Util.debugPrint("origin: " + area.origin);
        Util.debugPrint("size: " + area.size);
    }

    static void describeGobInArea() {
        System.out.println("click tow points to get area to get gob");
        Array<Gob> gobArray = ClickManager.getGobArrayInArea();
        for (Gob gob : gobArray)
            System.out.println("resource name: " + gob.resourceName());
    }

    static void printAutomationStackTrace() {
        AutomationManager.printStackTrace();
    }
}
