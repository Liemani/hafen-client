package lmi;

public class Constant {
    public static class Command {
        public static final String ACT = "act";
        public static final String LOGIN = "login";
        public static final String FOCUS = "focus";
        public static final String SELECT_CHARACTER = "play";   // args = { haven.widget <haven.Charlist>, String <character name> }
        public static final String CLICK = "click";
        public static final String FLOWER_MENU = "cl";
        public static final String AFK = "afk";
    }

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

    public static class InteractionType {
        public static final int GENERAL = 0;
        public static final int HAS_OVERLAY = 1;
    }

    public static class Input {
        public static class Mouse {
            public static final int LEFT = 1;
            public static final int MIDDLE = 2;
            public static final int RIGHT = 3;
        }

        public static class Modifier {
            public static final int NONE = 0;
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

    public static final double TILE_WIDTH_IN_COORD2D = 11.0;
    public static final int TILE_WIDTH_IN_COORD = 1024;
    public static final double COORD2D_PER_COORD = TILE_WIDTH_IN_COORD2D / TILE_WIDTH_IN_COORD;

    public static class Time {
        public static final long GENERAL_TIMEOUT = 1000;
        public static final long GENERAL_SLEEP = 500;
    }

    public static class MeshId {
        public static final int NONE = -1;
    }

    public static class HitBoxSize {
        public static class Width {
        }

        public static class Height {
        }
    }

    public static class gfx {
        public static class borka {
            public static final String BODY = "body";
        }

        public static class terobjs {
            public static class arch {
                public static final String PALISADEBIGGATE = "palisadebiggate";
                public static final String PALISADECP = "palisadecp";
                public static final String PALISADESEG = "palisadeseg";
                public static final String POLEBIGGATE = "polebiggate";
                public static final String POLECP = "polecp";
                public static final String POLESEG = "poleseg";
            }

            // gfx/terobjs/bumlings/basalt0
            // 0 ~ 3
            public static class bumlings {
                public static final String BASALT = "basalt";
                public static final String DOLOMITE = "dolomite";
                public static final String FLINT = "flint";
                public static final String GRANITE = "granite";
                public static final String LIMESTONE = "limestone";
                public static final String QUARTZ = "quartz";
                public static final String SCHIST = "schist";
            }

            public static class bushes {
                public static final String ARROWWOOD = "arrowwood";
                public static final String BLACKCURRANT = "blackcurrant";
                public static final String ELDERBERRYBUSH = "elderberrybush";
                public static final String GORSE = "gorse";
                public static final String HOLLY = "holly";
                public static final String REDCURRANT = "redcurrant";
                public static final String SPINDLEBUSH = "spindlebush";
            }

            public static class furn {
                public static final String BOUGHBED = "boughbed";
            }

            public static class items {
                public static final String BLACKCURRANTBERRY = "blackcurrantberry";
                public static final String BRANCH = "branch";
                public static final String CLOGS = "clogs";
                public static final String LIMESTONE = "limestone";
                public static final String QUARTZ = "quartz";
                public static final String SQUIRRELTAIL_BLOOD = "squirreltail-blood";
            }

            public static class plants {
                public static final String BARLEY = "barley";
                public static final String BEET = "beet";
                public static final String CARROT = "carrot";
                public static final String FLAX = "flax";
                public static final String HEMP = "hemp";
                public static final String LEEK = "leek";
                public static final String LETTUCE = "lettuce";
                public static final String PIPEWEED = "pipeweed";
                public static final String POPPY = "poppy";
                public static final String PUMPKIN = "pumpkin";
                public static final String REDONION = "redonion";
                public static final String TURNIP = "turnip";
                public static final String WHEAT = "wheat";
                public static final String YELLOWONION = "yellowonion";
            }

            // gfx/terobjs/stockpile-anyleather
            public static class stockpile {
                public static final String ANYLEATHER = "anyleather";
                public static final String BOARD = "board";
                public static final String BRANCH = "branch";
                public static final String BRICK = "brick";
                public static final String METAL = "metal";
                public static final String NUGGET_METAL = "nugget-metal";
                public static final String ORE = "ore";
                public static final String SOIL = "soil";
                public static final String STONE = "stone";
                public static final String WBLOCK = "wblock";
            }

            //  almondtree
            //  almondtreelog
            //  almondtreestump
            public static class trees {
                public static final String ALMONDTREE = "almondtree";
                public static final String APPLETREE = "appletree";
                public static final String BAYWILLOW = "baywillow";
                public static final String BEECH = "beech";
                public static final String BIRCH = "birch";
                public static final String BLACKPINE = "blackpine";
                public static final String CHECKERTREE = "checkertree";
                public static final String CHESTNUTTREE = "chestnuttree";
                public static final String CRABAPPLETREE = "crabappletree";
                public static final String DOGWOODLOG = "dogwoodlog";
                public static final String GRAYALDER = "grayalder";
                public static final String HAZEL = "hazel";
                public static final String PLUMTREE = "plumtree";
                public static final String POPLAR = "poplar";
                public static final String ROWAN = "rowan";
                public static final String SALLOW = "sallow";
                public static final String TEREBINTH = "terebinth";
                public static final String WILLOW = "willow";
            }

            public static class treesException {
                public static final String OLDSTUMP = "oldstump";
                public static final String OLDTRUNK = "oldtrunk";
            }

            public static class vehicle {
                public static final String CART = "cart";
                public static final String DUGOUT = "dugout";
                public static final String PLOW = "plow";
                public static final String ROWBOAT = "rowboat";
            }

            public static final String BARREL = "barrel";
            public static final String BEEHIVE = "beehive";
            public static final String CASTINGMOLD_SMALL = "castingmold-small";
            public static final String CHEST = "chest";
            public static final String CRATE = "crate";
            public static final String CRUCIBLE = "crucible";
            public static final String HEARTH_FIRE = "pow";
            public static final String PRIMSMELTER = "primsmelter";
            public static final String SURVOBJ = "survobj";
            public static final String TROUGH = "trough";
        }
    }
}
