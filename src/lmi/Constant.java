package lmi;

public class Constant {
    public static class Action {
        public static final String A_ACT = "act";
        public static final String A_LOGIN = "login";
        public static final String A_FOCUS = "focus";
        public static final String A_SELECT_CHARACTER = "play";   // args = { haven.widget <haven.Charlist>, String <character name> }
        public static final String A_CLICK = "click";
        public static final String A_CLOSE_FLOWER_MENU = "cl";
        public static final String A_AFK = "afk";
        public static final String A_CHANGE_CURSOR = "curs";
        public static final String A_GET = "click";
        public static final String A_PUT = "drop";

        public static enum Custom {
            AC_NONE,
            AC_MOVE_DID_BEGIN,
            AC_MOVE_DID_END,
            AC_DID_LIFT,
            AC_DID_PUT,
            AC_FLOWER_MENU_DID_ADDED,
            AC_PROGRESS_DID_ADDED,
            AC_PROGRESS_DID_DESTROYED,
        }
    }

    public static class SelfAction {
        public static final String A_POSE = "pose";
        public static final String A_DIG = "dig";
        public static final String A_MINE = "mine";
        public static final String A_CARRY = "carry";
        public static final String A_DESTROY = "destroy";
        public static final String A_FISH = "fish";
        public static final String A_INSPECT = "inspect";
        public static final String A_REPAIR = "repair";
        public static final String A_CRIME = "crime";
        public static final String A_SWIM = "swim";
        public static final String A_TRACKING = "tracking";
        public static final String A_AGGRO = "aggro";
        public static final String A_SHOOT = "shoot";
    }

    public static class InteractionType {
        public static final int IT_DEFAULT = 0;
        public static final int IT_HAS_OVERLAY = 1;
    }

    public static class Input {
        public static class Mouse {
            public static final int IM_LEFT = 1;
            public static final int IM_MIDDLE = 2;
            public static final int IM_RIGHT = 3;
        }

        public static class Modifier {
            public static final int IM_NONE = 0;
            public static final int IM_SHIFT = 1;
            public static final int IM_CONTROL = 2;
            public static final int IM_META = 4;
        }
    }

    public static class Gauge {
        public static class HitPointIndex {
            public static final int GI_HARD = 0;
            public static final int GI_SOFT = 1;
        }

        public static class Index {
            public static final int GI_HIT_POINT = 0;
            public static final int GI_STAMINA = 1;
            public static final int GI_ENERGY = 2;
        }
    }

    // flower menu petal name
    public static class Interaction {
        public static final String CHIP_STONE = "Chip stone";
        public static final String PICK = "Pick";
        public static final String CHOP_INTO_BLOCKS = "Chop into blocks";
        public static final String MAKE_BOARDS = "Make boards";
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

    public static final double TILE_WIDTH_IN_COORD2D = 11.0;
    public static final int TILE_WIDTH_IN_COORD = 1024;
    public static final double COORD2D_PER_COORD = TILE_WIDTH_IN_COORD2D / TILE_WIDTH_IN_COORD;

    public static class TimeOut {
        public static final long TO_MAX = Long.MAX_VALUE;
        public static final long TO_FAIL = 2000;
        public static final long TO_GENERAL = 1000;
        public static final long TO_TEMPORARY = 500;
        public static final long TO_NONE = 0;
    }

    public static class MeshId {
        public static final int MI_NONE = -1;
        public static final int MI_DEFAULT = 0;
    }

    public static class HitBoxSize {
        public static class Width {
        }

        public static class Height {
        }
    }

    public static class gfx {
        public static class hud {
            public static class meter {
                public static final String RN_HIT_POINT = "hp";
                public static final String RN_STAMINA = "stam";
                public static final String RN_ENERGY = "nrj";
            }

            public static class curs {
                public static final String RN_ARROW = "arw";
                public static final String RN_HAND = "hand";
            }
        }

        public static class borka {
            public static final String RN_BODY = "body";
            public static final String RN_IDLE = "idle";
            public static final String RN_WALKING = "walking";
            public static final String RN_BUCKETCARRY = "bucketcarry";
            public static final String RN_TURN = "turn";
            public static final String RN_CHOPPAN = "choppan";
            public static final String RN_SAWING = "sawing";
            public static final String RN_BUILDAN = "buildan";
            public static final String RN_THINKAN = "thinkan";
//              treepickan
        }

        public static class terobjs {
            public static class arch {
                public static final String RN_PALISADEBIGGATE = "palisadebiggate";
                public static final String RN_PALISADECP = "palisadecp";
                public static final String RN_PALISADESEG = "palisadeseg";
                public static final String RN_POLEBIGGATE = "polebiggate";
                public static final String RN_POLECP = "polecp";
                public static final String RN_POLESEG = "poleseg";
            }

            // gfx/terobjs/bumlings/basalt0
            // 0 ~ 3
            public static class bumlings {
                public static final String RN_BASALT = "basalt";
                public static final String RN_DOLOMITE = "dolomite";
                public static final String RN_FLINT = "flint";
                public static final String RN_GRANITE = "granite";
                public static final String RN_LIMESTONE = "limestone";
                public static final String RN_QUARTZ = "quartz";
                public static final String RN_SCHIST = "schist";
            }

            public static class bushes {
                public static final String RN_ARROWWOOD = "arrowwood";
                public static final String RN_BLACKCURRANT = "blackcurrant";
                public static final String RN_ELDERBERRYBUSH = "elderberrybush";
                public static final String RN_GORSE = "gorse";
                public static final String RN_HOLLY = "holly";
                public static final String RN_REDCURRANT = "redcurrant";
                public static final String RN_SPINDLEBUSH = "spindlebush";
            }

            public static class furn {
                public static final String RN_BOUGHBED = "boughbed";
            }

            public static class items {
                public static final String RN_BLACKCURRANTBERRY = "blackcurrantberry";
                public static final String RN_BRANCH = "branch";
                public static final String RN_CLOGS = "clogs";
                public static final String RN_LIMESTONE = "limestone";
                public static final String RN_QUARTZ = "quartz";
                public static final String RN_SQUIRRELTAIL_BLOOD = "squirreltail-blood";
            }

            public static class plants {
                public static final String RN_BARLEY = "barley";
                public static final String RN_BEET = "beet";
                public static final String RN_CARROT = "carrot";
                public static final String RN_FLAX = "flax";
                public static final String RN_HEMP = "hemp";
                public static final String RN_LEEK = "leek";
                public static final String RN_LETTUCE = "lettuce";
                public static final String RN_PIPEWEED = "pipeweed";
                public static final String RN_POPPY = "poppy";
                public static final String RN_PUMPKIN = "pumpkin";
                public static final String RN_REDONION = "redonion";
                public static final String RN_TURNIP = "turnip";
                public static final String RN_WHEAT = "wheat";
                public static final String RN_YELLOWONION = "yellowonion";
            }

            // gfx/terobjs/stockpile-anyleather
            public static class stockpile {
                public static final String RN_ANYLEATHER = "anyleather";
                public static final String RN_BOARD = "board";
                public static final String RN_BRANCH = "branch";
                public static final String RN_BRICK = "brick";
                public static final String RN_METAL = "metal";
                public static final String RN_NUGGET_METAL = "nugget-metal";
                public static final String RN_ORE = "ore";
                public static final String RN_SOIL = "soil";
                public static final String RN_STONE = "stone";
                public static final String RN_WBLOCK = "wblock";
            }

            //  almondtree
            //  almondtreelog
            //  almondtreestump
            public static class trees {
                public static final String RN_ALMONDTREE = "almondtree";
                public static final String RN_APPLETREE = "appletree";
                public static final String RN_BAYWILLOW = "baywillow";
                public static final String RN_BEECH = "beech";
                public static final String RN_BIRCH = "birch";
                public static final String RN_BLACKPINE = "blackpine";
                public static final String RN_CHECKERTREE = "checkertree";
                public static final String RN_CHESTNUTTREE = "chestnuttree";
                public static final String RN_CRABAPPLETREE = "crabappletree";
                public static final String RN_DOGWOODLOG = "dogwoodlog";
                public static final String RN_GRAYALDER = "grayalder";
                public static final String RN_HAZEL = "hazel";
                public static final String RN_PLUMTREE = "plumtree";
                public static final String RN_POPLAR = "poplar";
                public static final String RN_ROWAN = "rowan";
                public static final String RN_SALLOW = "sallow";
                public static final String RN_TEREBINTH = "terebinth";
                public static final String RN_WILLOW = "willow";
            }

            public static class treesException {
                public static final String RN_OLDSTUMP = "oldstump";
                public static final String RN_OLDTRUNK = "oldtrunk";
            }

            public static class vehicle {
                public static final String RN_CART = "cart";
                public static final String RN_DUGOUT = "dugout";
                public static final String RN_PLOW = "plow";
                public static final String RN_ROWBOAT = "rowboat";
            }

            public static final String RN_BARREL = "barrel";
            public static final String RN_BEEHIVE = "beehive";
            public static final String RN_CASTINGMOLD_SMALL = "castingmold-small";
            public static final String RN_CHEST = "chest";
            public static final String RN_CRATE = "crate";
            public static final String RN_CRUCIBLE = "crucible";
            public static final String RN_HEARTH_FIRE = "pow";
            public static final String RN_PRIMSMELTER = "primsmelter";
            public static final String RN_SURVOBJ = "survobj";
            public static final String RN_TROUGH = "trough";
        }
    }

    public static enum StatusCode {
        SC_SUCCEEDED,
        SC_INTERRUPTED,
        SC_TIME_OUT,
        SC_FAILED_INVALID_ARGUMENT,
        SC_FAILED_MATCH,
        SC_FAILED_OPEN_FLOWER_MENU,
        SC_FAILED_OPEN_PROGRESS,
        SC_FAILED_OPEN_WINDOW,
        SC_FAILED_CHOOSE,
        SC_FAILED_MOVE,
        SC_FAILED_LIFT,
        SC_FAILED_PUT,
    }
}
