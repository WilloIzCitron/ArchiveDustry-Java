package bluearchive.ui;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.scene.*;
import arc.scene.ui.Image;
import arc.util.Scaling;
import arc.util.Time;
import arc.util.Timer;
import mindustry.Vars;

import static mindustry.Vars.state;

public class ArchivDBackground {
    static boolean Animran = false;
    static Image img = new Image(new TextureRegion());
    public static void init(){
        Element tmp = Vars.ui.menuGroup.getChildren().first();
        if (!(tmp instanceof Group group)) return;
        Element render = group.getChildren().first();
        if (!(render.getClass().isAnonymousClass()
                && render.getClass().getEnclosingClass() == Group.class
                && render.getClass().getSuperclass() == Element.class)) return;
        render.visible = false;


        img.setFillParent(true);
            Time.runTask(12f, () -> {
                group.addChildAt(0, img);
                setRegion(img, "bluearchive-noa1");
                Timer timer = new Timer();
                Timer.Task task = new Timer.Task() {
                    @Override
                    public void run() {
                        if(state.isMenu()) {
                            wallpaperLoad();
                        }
                    }
                };
                timer.scheduleTask(task, 0f, 0.001f);
                timer.start();
                if(state.isMenu()) return ;

        });
    }


    public static void wallpaperLoad() {
        Animran = false;
            Time.run(5f, () -> {
                setRegion(img, "bluearchive-noa2");
                Time.clear();
                Time.run(5f, () -> {
                    setRegion(img, "bluearchive-noa3");
                    Time.clear();
                    Time.run(5f, () -> {
                        setRegion(img, "bluearchive-noa4");
                        Time.clear();
                        Time.run(5f, () -> {
                            setRegion(img, "bluearchive-noa5");
                            Time.clear();
                            Time.run(5f, () -> {
                                setRegion(img, "bluearchive-noa6");
                                Time.clear();
                                Time.run(5f, () -> {
                                    setRegion(img, "bluearchive-noa7");
                                    Time.clear();
                                    Time.run(5f, () -> {
                                        setRegion(img, "bluearchive-noa8");
                                        Time.clear();
                                        Time.run(5f, () -> {
                                            setRegion(img, "bluearchive-noa9");
                                            Time.clear();
                                            Time.run(5f, () -> {
                                                setRegion(img, "bluearchive-noa10");
                                                Time.clear();
                                                Time.run(5f, () -> {
                                                    setRegion(img, "bluearchive-noa11");
                                                    Time.clear();
                                                    Time.run(5f, () -> {
                                                        setRegion(img, "bluearchive-noa12");
                                                        Time.clear();
                                                        Time.run(5f, () -> {
                                                            setRegion(img, "bluearchive-noa13");
                                                            Time.clear();
                                                            Time.run(5f, () -> {
                                                                setRegion(img, "bluearchive-noa14");
                                                                Time.clear();
                                                                Time.run(5f, () -> {
                                                                    setRegion(img, "bluearchive-noa15");
                                                                    Time.clear();
                                                                    Time.run(5f, () -> {
                                                                        setRegion(img, "bluearchive-noa16");
                                                                        Time.clear();
                                                                        Time.run(5f, () -> {
                                                                            setRegion(img, "bluearchive-noa17");
                                                                            Time.clear();
                                                                            Time.run(5f, () -> {
                                                                                setRegion(img, "bluearchive-noa18");
                                                                                Time.clear();
                                                                                Time.run(5f, () -> {
                                                                                    setRegion(img, "bluearchive-noa19");
                                                                                    Time.clear();
                                                                                    Time.run(5f, () -> {
                                                                                        setRegion(img, "bluearchive-noa20");
                                                                                        Time.clear();
                                                                                        Time.run(5f, () -> {
                                                                                            setRegion(img, "bluearchive-noa21");
                                                                                            Time.clear();
                                                                                            Time.run(5f, () -> {
                                                                                                setRegion(img, "bluearchive-noa22");
                                                                                                Time.clear();
                                                                                                Time.run(5f, () -> {
                                                                                                    setRegion(img, "bluearchive-noa23");
                                                                                                    Time.clear();
                                                                                                    Time.run(5f, () -> {
                                                                                                        setRegion(img, "bluearchive-noa24");
                                                                                                        Time.clear();
                                                                                                        Time.run(5f, () -> {
                                                                                                            setRegion(img, "bluearchive-noa25");
                                                                                                            Time.clear();
                                                                                                            Time.run(5f, () -> {
                                                                                                                setRegion(img, "bluearchive-noa26");
                                                                                                                Time.clear();
                                                                                                                Time.run(5f, () -> {
                                                                                                                    setRegion(img, "bluearchive-noa27");
                                                                                                                    Time.clear();
                                                                                                                    Time.run(5f, () -> {
                                                                                                                        setRegion(img, "bluearchive-noa28");
                                                                                                                        Time.clear();
                                                                                                                        Time.run(5f, () -> {
                                                                                                                            setRegion(img, "bluearchive-noa29");
                                                                                                                            Time.clear();
                                                                                                                            Time.run(5f, () -> {
                                                                                                                                setRegion(img, "bluearchive-noa30");
                                                                                                                                Time.clear();
                                                                                                                                Time.run(5f, () -> {
                                                                                                                                    setRegion(img, "bluearchive-noa31");
                                                                                                                                    Time.clear();
                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                        setRegion(img, "bluearchive-noa32");
                                                                                                                                        Time.clear();
                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                            setRegion(img, "bluearchive-noa33");
                                                                                                                                            Time.clear();
                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                setRegion(img, "bluearchive-noa34");
                                                                                                                                                Time.clear();
                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                    setRegion(img, "bluearchive-noa35");
                                                                                                                                                    Time.clear();
                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                        setRegion(img, "bluearchive-noa36");
                                                                                                                                                        Time.clear();
                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                            setRegion(img, "bluearchive-noa37");
                                                                                                                                                            Time.clear();
                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                setRegion(img, "bluearchive-noa38");
                                                                                                                                                                Time.clear();
                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                    setRegion(img, "bluearchive-noa39");
                                                                                                                                                                    Time.clear();
                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                        setRegion(img, "bluearchive-noa40");
                                                                                                                                                                        Time.clear();
                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                            setRegion(img, "bluearchive-noa41");
                                                                                                                                                                            Time.clear();
                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                setRegion(img, "bluearchive-noa42");
                                                                                                                                                                                Time.clear();
                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                    setRegion(img, "bluearchive-noa43");
                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                        setRegion(img, "bluearchive-noa44");
                                                                                                                                                                                        Time.clear();
                                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                                            setRegion(img, "bluearchive-noa45");
                                                                                                                                                                                            Time.clear();
                                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                                setRegion(img, "bluearchive-noa46");
                                                                                                                                                                                                Time.clear();
                                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                                    setRegion(img, "bluearchive-noa47");
                                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                                        setRegion(img, "bluearchive-noa48");
                                                                                                                                                                                                        Time.clear();
                                                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                                                            setRegion(img, "bluearchive-noa49");
                                                                                                                                                                                                            Time.clear();
                                                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                                                setRegion(img, "bluearchive-noa50");
                                                                                                                                                                                                                Time.clear();
                                                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                                                    setRegion(img, "bluearchive-noa51");
                                                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                                                        setRegion(img, "bluearchive-noa52");
                                                                                                                                                                                                                        Time.clear();
                                                                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                                                                            setRegion(img, "bluearchive-noa53");
                                                                                                                                                                                                                            Time.clear();
                                                                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                                                                setRegion(img, "bluearchive-noa54");
                                                                                                                                                                                                                                Time.clear();
                                                                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                                                                    setRegion(img, "bluearchive-noa55");
                                                                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                                                                        setRegion(img, "bluearchive-noa56");
                                                                                                                                                                                                                                        Time.clear();
                                                                                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                                                                                            setRegion(img, "bluearchive-noa57");
                                                                                                                                                                                                                                            Time.clear();
                                                                                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                                                                                setRegion(img, "bluearchive-noa58");
                                                                                                                                                                                                                                                Time.clear();
                                                                                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                                                                                    setRegion(img, "bluearchive-noa59");
                                                                                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                                                                                        setRegion(img, "bluearchive-noa60");
                                                                                                                                                                                                                                                        Time.clear();
                                                                                                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                                                                                                            setRegion(img, "bluearchive-noa61");
                                                                                                                                                                                                                                                            Time.clear();
                                                                                                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                                                                                                setRegion(img, "bluearchive-noa62");
                                                                                                                                                                                                                                                                Time.clear();
                                                                                                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                                                                                                    setRegion(img, "bluearchive-noa63");
                                                                                                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                                                                                                        setRegion(img, "bluearchive-noa64");
                                                                                                                                                                                                                                                                        Time.clear();
                                                                                                                                                                                                                                                                        Time.run(5f, () -> {
                                                                                                                                                                                                                                                                            setRegion(img, "bluearchive-noa65");
                                                                                                                                                                                                                                                                            Time.clear();
                                                                                                                                                                                                                                                                            Time.run(5f, () -> {
                                                                                                                                                                                                                                                                                setRegion(img, "bluearchive-noa66");
                                                                                                                                                                                                                                                                                Time.clear();
                                                                                                                                                                                                                                                                                Time.run(5f, () -> {
                                                                                                                                                                                                                                                                                    setRegion(img, "bluearchive-noa67");
                                                                                                                                                                                                                                                                                    Time.clear();
                                                                                                                                                                                                                                                                                    Time.run(5f, () -> {
                                                                                                                                                                                                                                                                                        setRegion(img, "bluearchive-noa68");
                                                                                                                                                                                                                                                                                        Time.clear();
//                                                                                                                                                                                                                                                                                        Time.run(5f, () -> {
//                                                                                                                                                                                                                                                                                            setRegion(img, "bluearchive-noa1");
//                                                                                                                                                                                                                                                                                            Time.clear();
//                                                                                                                                                                                                                                                                                            Animran = true;
//                                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                                });
                                                                                                                                                                                                                                            });
                                                                                                                                                                                                                                        });
                                                                                                                                                                                                                                    });
                                                                                                                                                                                                                                });
                                                                                                                                                                                                                            });
                                                                                                                                                                                                                        });
                                                                                                                                                                                                                    });
                                                                                                                                                                                                                });
                                                                                                                                                                                                            });
                                                                                                                                                                                                        });
                                                                                                                                                                                                    });
                                                                                                                                                                                                });
                                                                                                                                                                                            });
                                                                                                                                                                                        });
                                                                                                                                                                                    });
                                                                                                                                                                                });
                                                                                                                                                                            });
                                                                                                                                                                        });
                                                                                                                                                                    });
                                                                                                                                                                });
                                                                                                                                                            });
                                                                                                                                                        });
                                                                                                                                                    });
                                                                                                                                                });
                                                                                                                                            });
                                                                                                                                        });
                                                                                                                                    });
                                                                                                                                });
                                                                                                                            });
                                                                                                                        });
                                                                                                                    });
                                                                                                                });
                                                                                                            });
                                                                                                        });
                                                                                                    });
                                                                                                });
                                                                                            });
                                                                                        });
                                                                                    });
                                                                                });
                                                                            });
                                                                        });
                                                                    });
                                                                });
                                                            });
                                                        });
                                                    });
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
    }
    private static void setRegion(Image img, String name) {
        img.getRegion().set(Core.atlas.find(name));
    }
}


