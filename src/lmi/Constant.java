package lmi;

// import haven
import haven.Coord;

public class Constant {
    public static enum Signal {
        S_NONE,
        S_MOVE_DID_BEGIN,
        S_MOVE_DID_END,
        S_DID_LIFT,
        S_DID_PUT,
        S_FLOWER_MENU_DID_ADDED,
        S_PROGRESS_DID_ADDED,
        S_PROGRESS_DID_DESTROYED,
        S_WINDOW_DID_ADDED,
        S_OBJECT_DID_CLICKED,
        S_AREA_DID_SELECTED,
        S_PLOB_DID_PLACED,
    }

    public static class Message {
        public static final String M_ACT = "act";   // { Action, ... }
        public static final String M_ACTIVATE = "activate";
        public static final String M_AFK = "afk";
        public static final String M_CL = "cl";
        public static final String M_CLICK = "click";
        public static final String M_DROP = "drop";
        public static final String M_ERROR = "error";
        public static final String M_FOCUS = "focus";
        public static final String M_LOGIN = "login";
        public static final String M_PLACE = "place";   // { Coord location, Int direction, int button, int modifier }
        public static final String M_PLAY = "play";   // { haven.widget <haven.Charlist>, String <character name> }
        public static final String M_TRANSFER = "transfer";
        public static final String M_XFER = "xfer";
    }

    public static class Action {
        public static final String A_AGGRO = "aggro";
        public static final String A_BP = "bp";
        public static final String A_CARRY = "carry";
        public static final String A_CRAFT = "craft";
        public static final String A_CRIME = "crime";
        public static final String A_DESTROY = "destroy";
        public static final String A_DIG = "dig";
        public static final String A_FISH = "fish";
        public static final String A_INSPECT = "inspect";
        public static final String A_MINE = "mine";
        public static final String A_POSE = "pose";
        public static final String A_REPAIR = "repair";
        public static final String A_SHOOT = "shoot";
        public static final String A_SWIM = "swim";
        public static final String A_TRACKING = "tracking";
    }

    public static class Plan {
        public static final String P_DFRAME = "dframe";
    }

    public static class WindowTitle {
        public static final String WT_DRYING_FRAME = "Drying Frame";
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
        public static final String CHOP_INTO_BLOCKS = "Chop into blocks";
        public static final String MAKE_BOARDS = "Make boards";
        public static final String PICK = "Pick";
        public static final String PICK_ALMOND = "Pick almond";
        public static final String PICK_BERRIES = "Pick berries";
        public static final String PICK_CATKIN = "Pick catkin";
        public static final String PICK_CHESTNUT = "Pick chestnut";
        public static final String PICK_CONE = "Pick cone";
        public static final String PICK_DRUPES = "Pick drupes";
        public static final String PICK_FRUIT = "Pick fruit";
        public static final String PICK_FRUITS = "Pick fruits";
        public static final String PICK_LEAF = "Pick leaf";
        public static final String PICK_SAMARA = "Pick samara";
        public static final String PICK_SEEDS = "Pick seeds";
        public static final String TAKE_BARK = "Take bark";
        public static final String TAKE_BOUGH = "Take bough";
        public static final String TAKE_BRANCH = "Take branch";
    }

    public static final double TILE_IN_COORD2D = 11.0;
    public static final int TILE_IN_COORD = 1024;
    public static final double COORD2D_PER_COORD = TILE_IN_COORD2D / TILE_IN_COORD;
    public static final double COORD_PER_COORD2D = TILE_IN_COORD / TILE_IN_COORD2D;
    public static final int CHUNK_SIDE = 9;
    public static final int VISIBLE_CHUNK_SIDE = 10;
    public static final int PURE_CHUNK_SIDE = VISIBLE_CHUNK_SIDE - 1;
    public static final int VISIBLE_TILE_SIDE = CHUNK_SIDE * VISIBLE_CHUNK_SIDE;
    public static final int PURE_TILE_SIDE = CHUNK_SIDE * PURE_CHUNK_SIDE;
    public static final int MAP_CHUNK_SIDE = 25;
    public static final int MAP_VISIBLE_CHUNK_SIDE = 5;

    public static final int RETRY_MAX = 5;

    public static final int D_EAST = 0;
    public static final int D_SOUTH = 16384;
    public static final int D_WEST = 32768;
    public static final int D_NORTH = 49152;

    public static class TimeOut {
        public static final long TO_MAX = Long.MAX_VALUE;
        public static final long TO_NONE = 0;
        public static final long TO_TEMPORARY = 500;
        public static final long TO_GENERAL = 1000;
        public static final long TO_RETRY = 5000;
        public static final long TO_WAIT = 300000;
    }

    public static class MeshId {
        public static final int MI_NONE = -1;
        public static final int MI_DEFAULT = 0;
    }

    public static class BoundingBox {
        public static final int BW_LOG = 352;
        public static final int BH_LOG = 1656;
        public static final Coord BB_LOG = Coord.of(BW_LOG, BH_LOG);
        public static final int BW_OLDTRUNK = 352;
        public static final int BH_OLDTRUNK = 1842;
        public static final Coord BB_OLDTRUNK = Coord.of(BW_OLDTRUNK, BH_OLDTRUNK);
        public static final int BW_BODY = 512;
        public static final int BH_BODY = 512;
        public static final Coord BB_BODY = Coord.of(BW_BODY, BH_BODY);
        public static final int BW_HORSE = 726;
        public static final int BH_HORSE = 1470;
        public static final Coord BB_HORSE = Coord.of(BW_HORSE, BH_HORSE);
        public static final int BW_CRATE = 1284;
        public static final int BH_CRATE = 726;
        public static final Coord BB_CRATE = Coord.of(BW_CRATE, BH_CRATE);
        public static final int BW_DRYING_FRAME = 2028;
        public static final int BH_DRYING_FRAME = 540;
        public static final Coord BB_DRYING_FRAME = Coord.of(BW_DRYING_FRAME, BH_DRYING_FRAME);
    }

    public static class gfx {
        public static class hud {
            public static class meter {
                public static final String RN_HIT_POINT = "hp";
                public static final String RN_STAMINA = "stam";
                public static final String RN_ENERGY = "nrj";
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
            public static final String RN_BANZAI = "banzai";
            public static final String RN_TREEPICKAN = "treepickan";
        }

        public static class invobjs {
            public static final String RN_BARKCORDAGE = "barkcordage";
            public static final String RN_HEMPFIBRE = "hempfibre";
            public static final String RN_HIDESTRAP = "hidestrap";
            public static final String RN_REEDTWINE = "reedtwine";
            public static final String RN_STRAWSTRING = "strawstring";
            public static final String RN_TOUGHROOT = "toughroot";

            public static class herbs {
                public static final String RN_CATTAILFIBRE = "cattailfibre";
                public static final String RN_SPINDLYTAPROOT = "spindlytaproot";
                public static final String RN_STINGINGNETTLE = "stingingnettle";
            }
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

            // example:
            //  almondtree
            //  almondtreelog
            //  almondtreestump
            public static class trees {
                public static final String RN_LOG = "log";
                public static final String RN_STUMP = "stump";
                public static final String RN_ALMONDTREE = "almondtree";
                public static final String RN_APPLETREE = "appletree";
                public static final String RN_BAYWILLOW = "baywillow";
                public static final String RN_BEECH = "beech";
                public static final String RN_BIRCH = "birch";
                public static final String RN_BLACKPINE = "blackpine";
                public static final String RN_CHECKERTREE = "checkertree";
                public static final String RN_CHESTNUTTREE = "chestnuttree";
                public static final String RN_CRABAPPLETREE = "crabappletree";
                public static final String RN_DOGWOOD= "dogwood";
                public static final String RN_GRAYALDER = "grayalder";
                public static final String RN_HAZEL = "hazel";
                public static final String RN_PLUMTREE = "plumtree";
                public static final String RN_POPLAR = "poplar";
                public static final String RN_ROWAN = "rowan";
                public static final String RN_SALLOW = "sallow";
                public static final String RN_TEREBINTH = "terebinth";
                public static final String RN_WILLOW = "willow";
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
            public static final String RN_STOCKPILE_BOUGH = "stockpile-bough";
            public static final String RN_STOCKPILE_BRANCH = "stockpile-branch";
            public static final String RN_CUPBOARD = "cupboard";
        }
    }

    public static final String[] nameSet_container = {
        gfx.terobjs.RN_CRATE,
        gfx.terobjs.RN_CHEST,
        gfx.terobjs.RN_CUPBOARD,
    };

    public static final String[] nameSet_string = {
        gfx.invobjs.RN_BARKCORDAGE,
        gfx.invobjs.RN_HEMPFIBRE,
        gfx.invobjs.RN_HIDESTRAP,
        gfx.invobjs.RN_REEDTWINE,
        gfx.invobjs.RN_STRAWSTRING,
        gfx.invobjs.RN_TOUGHROOT,
        gfx.invobjs.herbs.RN_CATTAILFIBRE,
        gfx.invobjs.herbs.RN_SPINDLYTAPROOT,
        gfx.invobjs.herbs.RN_STINGINGNETTLE,
    };

    public static enum ExceptionType {
        // why
        ET_INTERRUPTED,
        ET_TIME_OUT,
        ET_TOO_SMALL_SPACE,
        ET_NO_INPUT,
        ET_FULL_OUTPUT,
        ET_COMMAND_ERROR,
        ET_COMMAND_HELP,
        ET_COMMAND_INITIALIZER,
        ET_COMMAND_IMPLEMENT,
        ET_MAN_IMPLEMENT,
        ET_NO_PATH,

        // fail
        ET_MOVE,
        ET_FLOWER_MENU_OPEN,
        ET_FLOWER_MENU_CHOOSE,
        ET_FLOWER_MENU_MATCH,
        ET_COMMAND_MATCH,
        ET_PROGRESS_OPEN,
        ET_LIFT,
        ET_PUT,
        ET_DECIDE_PLAN,
    }
}
