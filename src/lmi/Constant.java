package lmi;

public class Constant {
    public static class Command {
        public static final String ACTION = "act";
        public static class Action {
            public static final String DIG = "dig";
            public static final String MINE = "mine";
            public static final String CARRY = "carry";
            public static final String DESTROY = "destroy";
            public static final String FISH = "fish";
            public static final String INSPECT = "inspect";
            public static final String REPAIR = "repair";
            public static final String CRIME = "crime";
            public static final String SWIM = "swim";
            public static final String TRACKING = "tracking";
            public static final String AGGRO = "aggro";
            public static final String SHOOT = "shoot";
        }

        public static final String LOGIN = "login";
        public static final String FOCUS = "focus";
        // haven.Widget sender = haven.Charlist, String = character name
        public static final String SELECT_CHARACTER = "play";
        public static final String CLICK = "click";
        public static final String FLOWER_MENU = "cl";
        public static final String AFK = "afk";
    }

    public static class Mouse {
        public static class Button {
            public static final int LEFT = 1;
            public static final int MIDDLE = 2;
            public static final int RIGHT = 3;
        }

        public static class Modifier {
            public static final int SHIFT = 1;
            public static final int CONTROL = 2;
            public static final int META = 4;
        }
    }

    public static class Gauge {
        public static class ResourceName {
            public static final String HIT_POINT = "gfx/hud/meter/hp";
            public static final String STAMINA = "gfx/hud/meter/stam";
            public static final String ENERGY = "gfx/hud/meter/nrj";
        }

        public static class HitPointIndex {
            public static final int HARD = 0;
            public static final int SOFT = 1;
        }

        public static class Index {
            public static final int HIT_POINT = 0;
            public static final int STAMINA = 1;
            public static final int ENERGY = 2;
        }
    }

    // flower menu petal name
    public static class Interaction {
        public static final String CHIP_STONE = "Chip stone";
        public static final String PICK = "Pick";
        public static final String PICK_ALMOND = "Pick almond";
        public static final String PICK_BERRIES = "Pick berries";
        public static final String PICK_CATKIN = "Pick catkin";
        public static final String PICK_CHESTNUT = "Pick chestnut";
        public static final String PICK_CONE = "Pick cone";
        public static final String PICK_DRUPES = "Pick drupes";
        public static final String PICK_LEAF = "Pick leaf";
        public static final String PICK_FRUIT = "Pick fruit";
        public static final String PICK_FRUITS = "Pick fruits";
        public static final String PICK_SAMARA = "Pick samara";
        public static final String PICK_SEEDS = "Pick seeds";
        public static final String TAKE_BARK = "Take bark";
        public static final String TAKE_BOUGH = "Take bough";
        public static final String TAKE_BRANCH = "Take branch";
    }

    public static class Time {
        public static final long GENERAL_TIMEOUT = 1000;
        public static final long GENERAL_SLEEP = 500;
    }

    public static class MeshId {
        public static final int NONE = -1;
    }
}
