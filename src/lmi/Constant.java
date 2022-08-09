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
}
