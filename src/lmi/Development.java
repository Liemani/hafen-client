package lmi;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeMap;

import haven.*;

import lmi.AutomationManager.Automation;
import lmi.automation.*;
import static lmi.Api.*;
import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.MeshId.*;
import static lmi.Constant.gfx.borka.*;
import static lmi.Constant.BoundingBox.*;
import static lmi.Constant.*;
import static lmi.Constant.Plan.*;
import static lmi.Constant.WindowTitle.*;
import static lmi.Constant.Input.Mouse.*;
import static lmi.Constant.Input.Modifier.*;
import static lmi.Constant.Action.*;
import static lmi.Constant.Message.*;

public class Development implements Console.Command {
    // Type Define
    private static class CommandMap extends TreeMap<String, Method> {};

    // Field
    private static CommandMap _commandMap;
    private static String[] _args;

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

    private static void setArgument(String[] args) { _args = args; }

    // Run
    public void run(Console cons, String[] args) throws Exception {
        Development.setArgument(args);
        new Thread(new MainRunnable(args)).start();
    }

    private static class MainRunnable implements Runnable {
        private String[] _args;
        private MainRunnable(String[] args) { _args = args; }

        // Runnable Requirment
        public void run() {
            Api.message("==============================");
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
                            Api.error("[" + (commandString != null ? commandString : "") + "]가 뭔지 모르겠어요");
                            break;
                        default:
                            break;
                    }
                } else {
                    Util.debugPrint(e);
                    e.printStackTrace();
                }
            } finally {
                Api.message("[terminating dev]");
            }
        }

        private void _checkArgument() {
            if (_args.length == 1) throw new LMIException(ET_COMMAND_ERROR);
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
            Api.message("  [dev Manual]");
            Api.error("usage: dev <command>");
            writer.println(" ");
            Api.message("command list:");
            _printCommandStringList(writer);
        }

        private static void _printHelp(PrintWriter writer) {
            Api.message("  [dev Manual]");
            Api.error("usage: dev <command>");
            writer.println(" ");
            Api.message("command list:");
            _printCommandStringList(writer);
        }

        private static void _printCommandStringList(PrintWriter printWriter) {
            for (String command : Development.getCommandStringSet())
                Api.message("  " + command);
        }
    }

    static void listConsoleCommand() {
        Api.message("console command:");
        for (String command : Util.consoleCommands())
            Api.message("  " + command);
    }

    static void printWidgetTree() {
        final Widget rootWidget = ObjectShadow.rootWidget();
        iterateWidget(rootWidget, 0);
    }

    private static void iterateWidget(haven.Widget widget, int indentCount) {
        for (; widget != null; widget = widget.next) {
            Util.insertIndent(indentCount);
            System.out.println(widget.getClass().getName());
            iterateWidget(widget.child, indentCount + 1);
        }
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
    static void h() { moveWest(); }
    static void j() { moveSouth(); }
    static void k() { moveNorth(); }
    static void l() { moveEast(); }
    static void moveCenter() { Api.moveCenter(); }

    static void jIterate() {
        if (_args.length != 3) {
            Api.error("usage: dev jIterate <start distance>");
            return;
        }

        Api.alert("바운딩 박스의 크기를 측정할 Gob을 클릭해 주세요");
        Gob gob = Api.getGob();

        int targetDistance = Integer.parseInt(_args[2]);
        Api.move(gob.location().assignSubtract(0, targetDistance));

        while (true) {
            final Coord destination = Self.location().add(0, 1);
            Api.move(destination);
            System.out.println("current location: " + Self.location());
        }
    }

    static void describeCursorGItem() {
        haven.Widget gItem = WidgetManager.cursorGItem();
        Debug.describeField(gItem);
    }

    static void describeGob() {
        Api.alert("정보를 출력할 오브젝트를 클릭해 주세요");
        Gob gob = ClickManager.getGob();
        Api.message(gob.debugDescription());
    }

//      static void describeClosestGobOverlay() {
//          Gob closestGob = closestGob();
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
//          Gob closestGob = closestGob();
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

//      static void gatherClosestGob() {
//          String action = lmi.Scanner.nextLineWithPrompt("enter action");
//          Gob closestGob = closestGob();
//          FlowerMenuHandler.choose(closestGob, MI_DEFAULT, action);
//          moveNorth();
//      }

    static void investigateGobBoundingBoxWidth() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        Gob variantGob = ClickManager.getGob();

        Coord standardPoint = Self.location();
        Api.lift(standardGob);
        Api.move(standardPoint.add(0, 2048));
        Api.put(standardPoint);

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
        Api.lift(gob);
        Api.move(putPoint.add(0, TILE_IN_COORD * 2));
        Api.put(putPoint);
    }

    static void investigateGobBoundingBoxHeight() {
        System.out.println("click gob of standard!");
        Gob standardGob = ClickManager.getGob();

        System.out.println("click next gob to move!");
        Gob variantGob = ClickManager.getGob();

        Coord standardPoint = Self.location().center();
        Api.lift(standardGob);
        Api.move(standardPoint.add(0, 2048));
        Api.put(standardPoint);

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
        Api.lift(gob);
        Api.put(putPoint);
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

        Api.move(firstStep);
        Api.move(variantPoint);

        System.out.println("succeeded coord: " + variantPoint
                + ", distance: " + Self.distance(standardGob));
    }

    static void putLogDistanceBodyWidth() {
        final Coord origin = Self.location().center();

        System.out.println("첫 번째 로그를 선택해주세요");
        final Gob firstLog = Api.getGob();

        System.out.println("두 번째 로그를 선택해주세요");
        final Gob secondLog = Api.getGob();

        final int averageY = (BH_LOG + BH_BODY) / 2;
        final int averageX = (BW_LOG + BW_BODY) / 2;

        Api.lift(firstLog);
        Api.move(origin.add(-averageX - 1, averageY));
        Api.put(origin.add(-averageX - 1, 0));

        Api.lift(secondLog);
        Api.move(origin.add(averageX + 1, averageY));
        Api.put(origin.add(averageX + 1, 0));

    }

    static void describeGobInArea() {
        System.out.println("출력할 gob이 있는 영역을 선택해주세요");
        Array<Gob> gobArray = getGobArrayInArea();
        for (Gob gob : gobArray)
            System.out.println("resource name: " + gob.resourceName());
    }

    static void printAutomationStackTrace() {
        AutomationManager.printStackTrace();
    }

    static void prepare() {
        // set chat channel
        WidgetManager.chatUI().selectSystem();

        // set debug print on
        Debug.toggleIsPrint();

        // set grid on
        ObjectShadow.mapView().showgrid(true);

        // set item quality on
    }

    static void interact() {
        if (_args.length >= 3)
            Api.interact(Api.closestGobOf(_args[2]));
        else
            Api.interact(Api.closestGob());
    }

    static void lift() {
        if (_args.length >= 3)
            Api.lift(Api.closestGobOf(_args[2]));
        else
            Api.lift(Api.closestGob());
    }

    static void printAllISBoxText() {
        Window window = WidgetManager.gameUI().getChildOf(Window.class);
        if (window == null) return;
        Widget child = window.child;
        while (child != null) {
            if (child instanceof ISBox) {
                final ISBox isbox = (ISBox)child;
                Util.debugPrint("isbox.text(): " + isbox.text());
            }
            child = child.next;
        }
    }

    static void measure() {
        if (_args.length != 3) {
            Api.error("usage: dev measure <start distance>");
            return;
        }

        Api.alert("바운딩 박스의 크기를 측정할 Gob을 클릭해 주세요");
        Gob gob = Api.getGob();

        int lastFailedDistance = 0;
        int succeededDistance = Integer.parseInt(_args[2]);

        final Coord gobLocation = gob.location();

        final Coord branch = Coord.of(gobLocation).assignAdd(0, TILE_IN_COORD * -2);
        final Coord leaf = Coord.of(gobLocation);

        while (true) {
            final int currentDistance = (lastFailedDistance + succeededDistance) / 2;
            final int targetX = gobLocation.x + currentDistance;
            leaf.x = targetX;
            branch.x = targetX;
            Api.move(branch);
            try {
                Api.move(leaf);
                succeededDistance = currentDistance;
            } catch (LMIException e) {
                if (e.type != ET_MOVE) throw e;
                lastFailedDistance = currentDistance;
            }
            Util.debugPrint(Self.location());
            if (succeededDistance - lastFailedDistance == 1)
                break;
        }
        Util.debugPrint("lastFailedDistance: " + lastFailedDistance);
        Util.debugPrint("succeededDistance: " + succeededDistance);
    }

    static void test000() {
        Api.planAndCarryOutObject(P_DFRAME, Self.location().north().north(), 0);
    }

    static void test001() {
        Api.message(WidgetManager.isbox().text());
    }

    static void test002() {
        Api.message("" + WidgetManager.isbox().count());
    }

    static void test003() {
        WidgetManager.gitem().describe();
    }

    static void test004() {
        if (_args.length != 3) {
            Api.error("usage: dev test004 <count to transfer>");
            return;
        }
        final int count = Integer.parseInt(_args[2]);

        Api.alert("string item을 가져올 container를 클릭해주세요");
        takeItemFromContainer(Api.getGob(), lmi.Constant.nameSet_string, count);
    }

    static void test005() {
        final boolean didPlanObject = ObjectShadow.mapView().didPlanObject();
        Api.message("MapView에 plan이 됐는가: " + didPlanObject);
    }
}
