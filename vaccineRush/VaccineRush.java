package vaccineRush;

import myGameEngine.Actions.*;
import myGameEngine.Actions.Avatar.*;
import myGameEngine.ManualObject.*;
import myGameEngine.Player.*;
import myGameEngine.Controllers.*;
import myGameEngine.Cameras.*;
import myGameEngine.NPC.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;
import java.text.DecimalFormat;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rage.util.Configuration;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.physics.PhysicsEngine;
import ray.physics.PhysicsEngineFactory;
import ray.physics.PhysicsObject;
import ray.physics.JBullet.JBulletUtils;
import ray.networking.IGameConnection.ProtocolType;
import ray.audio.*;
import static ray.rage.scene.SkeletalEntity.EndType;

import com.jogamp.openal.ALFactory;

import net.java.games.input.Controller;
import net.java.games.input.Event;

public class VaccineRush extends VariableFrameRateGame 
{
	
	//Global Managers
	private InputManager im;
	private TextureManager tm;
	private RenderSystem rs;
	public SceneManager sm;
	IAudioManager audioMgr;
	
	//Camera Variables
	private StretchController playerStretchContr;
	private OrbitController playerOrbitContr;
	private CameraController3P orbitCamOne;

	//Action List Variables
	private Action moveLeftPlayerOne, moveRightPlayerOne, moveForwardPlayerOne, 
				moveBackwardPlayerOne, rotateLeftAction1, rotateLeftAction2, rotateRightAction1, rotateRightAction2,
				rotateYawAxisAction, moveYAxisAction, moveXAxisAction, germBoostAction, throwGermAction,activateGermAction, placeGermAction, quitGameAction;

	
	// Server Variables
	private ProtocolType serverProtocol;
	private Vector<UUID> gameObjectsToRemove;
	private int serverPort;
	private String serverAddress;
	private Tessellation tessellationE;
	
	//Audio Variables
	Sound coughSound, clapSound, bgSound;
	
	
	public Camera cameraOne;
	
	//Scene Node Variables
	public SceneNode cameraOneN, avatarN, npc1N, blueFlagN, redFlagN, greenFlagN, orangeFlagN, tent1N, tent2N, hospitalN, radioStationN,radioStation2N,
					 monkeyBuildingN, germballN, groundN, originN, xAxisN, yAxisN, zAxisN, cubeOneN, cubeTwoN, tessellationN, turnInFlagN;
	public Entity redFlagE, blueFlagE, greenFlagE, orangeFlagE, tent1E, tent2E, hospitalE, radioStationE, radioStation2E, monkeyBuildingE, germballE, turnInFlagE;
	public SkeletalEntity avatarE, npc1E;
	
	//Booleans
	public boolean active = true, playerHasRedFlag = false, playerHasBlueFlag = false, playerHasGreenFlag = false, germActive = false, playerHasOrangeFlag = false;
	private boolean isClientConnected, playerWon = false, playerLost = false;
	
	//Physics Variables
	public PhysicsEngine physicsEng;
	public PhysicsObject avatarPhysObj, groundPhysObj;
	public HashMap<SceneNode, Boolean> currPlanets = new HashMap<SceneNode, Boolean>();
	public String groundE_string = "Ground";
	public String groundN_string = "GroundNode";
	private static final String SKYBOX_NAME = "SkyBox";
	
	// Scripting Variables
	protected ScriptEngine jsEngine;
	protected File setupSkybox, setupTerrain;
	
	//Ints, Floats,Doubles,Strings, Random, Lists
	float elapsTime = 0.0f, movemt = .01f;
	String elapsTimeStr, counterStr, dispStr1, dispStr2, avatarStr, positionStr, playerOneScoreStr, playerOneLivesStr;
	int elapsTimeSec, counter = 0, playerOneScore = 0, playerOneLives = 3, germActiveCount = 0;
	private static final Random rand = new Random();
	ArrayList<SceneNode> germNodes = new ArrayList<SceneNode>();
	

	
    
	
	public VaccineRush() 
	{
        super();
//        this.serverAddress = serverAddr;
//        this.serverPort = sPort;
//        this.serverProtocol = ProtocolType.UDP;
        System.out.println("Keyboard:");
		System.out.println("press W/S to move foward and backward.");
		System.out.println("press A/D to turn character left and right.");
		System.out.println("press Q/E to turn character and camera left and right.");
		System.out.println("press Left/Right arrow to turn camera left and right!");
		System.out.println("press Up/Down arrow to turn camera up and down!");
		System.out.println("press +/- on the keypad to zoom in and out!");
		System.out.println("press Spacebar to cough a germ!");
		System.out.println("press Left Ctrl to place a germ!");
		System.out.println("press N to toggle God mode!");
		System.out.println("press Escape to quit!");
		System.out.println("====================================================================");
		System.out.println("Controller:");
		System.out.println("use Left Joystick to move!");
		System.out.println("use Right Joystick to look around!");
		System.out.println("press RT/LT to zoom in and out!");
		System.out.println("press RB/LB to turn your character!");
		System.out.println("press A to cough a germ!");
		System.out.println("press B to place a germ!");
		System.out.println("click Left Joystick to quit!");
		System.out.println("====================================================================");		
		
    }

    public static void main(String[] args) 
    {
        Game game = new VaccineRush();
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
        }
    }
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) 
	{
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		DisplaySettingsDialog dsd = new DisplaySettingsDialog(ge.getDefaultScreenDevice());
		dsd.showIt();
		RenderWindow rw = rs.createRenderWindow(dsd.getSelectedDisplayMode(), dsd.isFullScreenModeSelected());
	}
	
	 public void setupWindowViewports(RenderWindow rW)
	    {
	    	rW.addKeyListener(this);
	    	rW.addMouseListener(this);
	    	rW.addMouseMotionListener(this);
	    	rW.addMouseWheelListener(this);
	    	
	    	Viewport playerOneView = rW.getViewport(0);
	    }

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) 
    {
        
    	SceneNode rootNode = sm.getRootSceneNode();
 
    	// Camera One
    	cameraOne = sm.createCamera("cameraOne", Projection.PERSPECTIVE);;
	    rw.getViewport(0).setCamera(cameraOne);
	   
	    SceneNode cameraOneN = rootNode.createChildSceneNode(cameraOne.getName() + "Node");
	    cameraOneN.attachObject(cameraOne);
	    cameraOne.setMode('n');
	    cameraOne.getFrustum().setFarClipDistance(1000.0f);
		
    }
	
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException
    {
    	this.sm = sm;
 
    	// Create Managers & Render System
    	if (tm == null){tm = eng.getTextureManager();}
    	if (im == null){im = new GenericInputManager();}
    	if (rs == null){rs = (GL4RenderSystem)eng.getRenderSystem();}
   
    	// Create the Skybox with script
    	ScriptEngineManager factory = new ScriptEngineManager();
    	java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();
    	jsEngine = factory.getEngineByName("js");
    	
    	Invocable invocableEngine = (Invocable) jsEngine;
    	setupSkybox = new File("setupSkybox.js");
    	this.runScript(jsEngine, setupSkybox);
    	
    	try
    	{
    		invocableEngine.invokeFunction("setupSkybox",  sm, eng, tm);
    	}catch (ScriptException e1)
    	{
    		System.out.println("ScriptException in " + setupSkybox + e1); 
		}catch (NoSuchMethodException e2)
		{   
			System.out.println("No such method in " + setupSkybox + e2); 
		}catch (NullPointerException e3)
		{ 
			System.out.println ("Null ptr exception reading " + setupSkybox + e3);
		}
    	
    	//Create Terrain with Script
    	setupTerrain = new File("setupTerrain.js");
    	this.runScript(jsEngine, setupTerrain);
    	
    	try
    	{
    		invocableEngine.invokeFunction("setupTessellation",  this);
    	}catch (ScriptException e1)
    	{
    		System.out.println("ScriptException in " + setupTerrain + e1); 
		}catch (NoSuchMethodException e2)
		{   
			System.out.println("No such method in " + setupTerrain + e2); 
		}catch (NullPointerException e3)
		{ 
			System.out.println ("Null ptr exception reading " + setupTerrain + e3);
		}
    	
    	tessellationE = (Tessellation) jsEngine.get("tessellationEntity");
    	tessellationN = (SceneNode) jsEngine.get("tessellationNode");
    	
    	
    	
    	//Avatar
        avatarE = sm.createSkeletalEntity("avatar", "mohawkman.rkm", "mohawkman.rks");
        Texture avatarT = sm.getTextureManager().getAssetByPath("mohawkman.jpeg");
        TextureState avatarTS = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        avatarTS.setTexture(avatarT);
        avatarE.setRenderState(avatarTS); 
        avatarE.setPrimitive(Primitive.TRIANGLES);
        avatarN = sm.getRootSceneNode().createChildSceneNode(avatarE.getName() + "Node");
        avatarN.scale(.1f,.1f,.1f);
        avatarN.attachObject(avatarE);
        avatarN.moveLeft(1.0f);
        avatarN.setLocalPosition(-10, 0, -10);
        
        avatarE.loadAnimation("coughAction", "coughAction.rka");
        avatarE.loadAnimation("clapAction", "clapAction.rka");

        //NPC Monkey - Radio Station
   
        npc1E = sm.createSkeletalEntity("npc1", "morgan.rkm", "morgan.rks");
        Texture monkeyT = sm.getTextureManager().getAssetByPath("morgan.jpeg");
        TextureState monkeyTS = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        monkeyTS.setTexture(monkeyT);
        npc1E.setRenderState(monkeyTS); 
        npc1E.setPrimitive(Primitive.TRIANGLES);
        npc1N = sm.getRootSceneNode().createChildSceneNode(npc1E.getName() + "Node");
        npc1N.scale(.8f,.8f,.8f);
        npc1N.attachObject(npc1E);
        npc1N.moveLeft(1.0f);
        npc1N.setLocalPosition(-47.86f, 0, 63.44f);
        
        npc1E.loadAnimation("bladeAction", "blade.rka");
        npc1E.loadAnimation("walkAction", "walk.rka");
        
     
        //Flag Setup
        createBlueFlag();
        createRedFlag();
        createGreenFlag();
        createOrangeFlag();
        
        //Hospital Flag
        turnInFlagE = sm.createEntity("turnInFlag", "flag.obj");
        turnInFlagE.setPrimitive(Primitive.TRIANGLES);
        turnInFlagN = sm.getRootSceneNode().createChildSceneNode("turnInFlagNode");
        turnInFlagN.scale(.2f,.1f,.2f);
        turnInFlagN.attachObject(turnInFlagE);
        turnInFlagN.moveDown(10.0f);
        turnInFlagN.setLocalPosition(0.95f, 0f, -3.04f);
        
        //Quarantine Tents
        tent1E = sm.createEntity("tent1", "tent.obj");
        tent1E.setPrimitive(Primitive.TRIANGLES);
        tent1N = sm.getRootSceneNode().createChildSceneNode("tent1Node");
        tent1N.scale(.5f,.5f,.5f);
        tent1N.attachObject(tent1E);
        tent1N.moveDown(10.0f);
        tent1N.setLocalPosition(46.8f, -1.0f, 5f);
        
        tent2E = sm.createEntity("tent2", "tent.obj");
        tent2E.setPrimitive(Primitive.TRIANGLES);
        tent2N = sm.getRootSceneNode().createChildSceneNode("tent2Node");
        tent2N.scale(.5f,.5f,.5f);
        tent2N.attachObject(tent2E);
        tent2N.moveDown(10.0f);
        tent2N.setLocalPosition(-54.8f, -1.0f, 4.6f);
        
        //Main Hospital
        hospitalE = sm.createEntity("hospital", "hospital.obj");
        hospitalE.setPrimitive(Primitive.TRIANGLES);
        hospitalN = sm.getRootSceneNode().createChildSceneNode("hospitalNode");
        hospitalN.scale(.5f,.5f,.5f);
        hospitalN.attachObject(hospitalE);
        hospitalN.moveDown(10.0f);
        hospitalN.setLocalPosition(0f, -.5f, 0f);
        
        //Radio Station
      //Main Hospital
        radioStationE = sm.createEntity("radioStation", "radioStation.obj");
        radioStationE.setPrimitive(Primitive.TRIANGLES);
        radioStationN = sm.getRootSceneNode().createChildSceneNode("radioStationNode");
        radioStationN.scale(.5f,.5f,.5f);
        radioStationN.attachObject(radioStationE);
        radioStationN.moveDown(10.0f);
        radioStationN.setLocalPosition(-49.23f, 0f, 49.92f);
//        radioStationN.setLocalRotation(
//        				[  0.95106 |   0.00000 |  -0.30902]
//        	           [  0.00000 |   1.00000 |   0.00000]
//        	           [  0.30902 |   0.00000 |   0.95106]);
        
        //Radio Station #2
        radioStation2E = sm.createEntity("radioStation2", "radioStation2.obj");
        radioStation2E.setPrimitive(Primitive.TRIANGLES);
        radioStation2N = sm.getRootSceneNode().createChildSceneNode("radioStation2Node");
        radioStation2N.scale(.5f,.5f,.5f);
        radioStation2N.attachObject(radioStation2E);
        radioStation2N.moveDown(10.0f);
        radioStation2N.setLocalPosition(97.78f, 0f, -50.61f);
        
//        
//        
        //Monkey Building
        monkeyBuildingE = sm.createEntity("monkeyBuilding", "monkeyBuilding.obj");
        monkeyBuildingE.setPrimitive(Primitive.TRIANGLES);
        monkeyBuildingN = sm.getRootSceneNode().createChildSceneNode("monkeyBuildingNode");
        monkeyBuildingN.scale(.5f,.5f,.5f);
        monkeyBuildingN.attachObject(monkeyBuildingE);
        monkeyBuildingN.moveDown(10.0f);
        monkeyBuildingN.setLocalPosition(51.3f, -.5f, 55.60f);
    

        //Avatar Textures
//        Texture avatarT = tm.getAssetByPath("blue.jpeg");
//        TextureState avatarTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
//        avatarTS.setTexture(avatarT);
//        avatarE.setRenderState(avatarTS);

    	// Initialize the Input Manager
    	setupInputs(sm);
    	   
    	// Controllers
    	playerStretchContr = new StretchController();
    	playerOrbitContr = new OrbitController(avatarN, 1.0f, 0.5f, 100.0f, false);
    	sm.addController(playerOrbitContr);
    	
        //Ambient Lighting
        sm.getAmbientLight().setIntensity(new Color(.1f, .1f, .1f));
        
        //Main light
        Light mainLight = sm.createLight("mainLight", Light.Type.POINT);
        mainLight.setAmbient(new Color(.5f, .5f, .5f));
        mainLight.setDiffuse(new Color(.8f, .8f, .8f));
        mainLight.setSpecular(new Color(.5f, .5f, .5f));
        SceneNode mainLightNode = sm.getRootSceneNode().createChildSceneNode(mainLight.getName() + "Node");
        mainLightNode.moveUp(400.0f);
        mainLightNode.attachObject(mainLight);
        
        //Initialize node for point light for Dolphin 1
		Light plight1 = sm.createLight("plight1", Light.Type.POINT);
		plight1.setAmbient(new Color(.3f, .3f, .3f));
        plight1.setDiffuse(new Color(.7f, .7f, .7f));
		plight1.setSpecular(new Color(.5f, .5f, .5f));
		plight1.setConeCutoffAngle(Degreef.createFrom(20.0f));
		plight1.setQuadraticAttenuation(.005f);
		plight1.setConstantAttenuation(0.3f);
		plight1.setFalloffExponent(40.0f);
		plight1.setLinearAttenuation(.05f);
        plight1.setRange(50.0f);
		
		SceneNode plight1Node = avatarN.createChildSceneNode("plight1Node");
        plight1Node.attachObject(plight1);
        
        // Physics
        
        ManualObject groundE = ManualObjectFloor.manualObjectFloor(eng, sm);
        groundN = sm.getRootSceneNode().createChildSceneNode(groundN_string);
        groundN.attachObject(groundE);
        groundN.setLocalPosition(0.0f, -1.0f, 0.0f);
        
        setupPhysics();
        setupPhysicsWorld();
        setupOrbitCameras(eng, sm);
        
        //audio
        initAudio(sm);
      
    }
    
    protected void setupOrbitCameras(Engine eng, SceneManager sm)
    {
    	String keyboardName, contrName;
    	keyboardName = im.getKeyboardName();
    	contrName = im.getFirstGamepadName();

    	SceneNode cameraOneN = sm.getSceneNode("cameraOneNode");
    	cameraOne = sm.getCamera("cameraOne");
    	orbitCamOne = new CameraController3P(cameraOne, cameraOneN, avatarN, keyboardName, contrName, im);
    }
    
    //Random Coordinate Generator
    float[] randomCoordArray(float upperBound)
    {
    	float[] randomCoord =
    		{
    				(rand.nextFloat() * upperBound), (rand.nextFloat() * upperBound), (rand.nextFloat() * upperBound) 
    		};
    	return randomCoord;
    }
    
    // Initialize Key binds and Controller Input
    protected void setupInputs(SceneManager sm)
    {	
    	String gamepadName = im.getFirstGamepadName();
    	String keyboardName = im.getKeyboardName();
    	
    	// Initialize our Action Objects to link to Keys
    	
    	activateGermAction = new ActivateGermAction(avatarN, this);
    	moveLeftPlayerOne = new AvatarMoveLeftAction(avatarN, this);
    	moveRightPlayerOne = new AvatarMoveRightAction(avatarN);
    	moveForwardPlayerOne = new AvatarForwardAction(avatarN, this);
    	moveBackwardPlayerOne = new AvatarBackwardAction(avatarN, this);
    	moveXAxisAction = new MoveXAxisAction(this, avatarN);
    	moveYAxisAction = new MoveYAxisAction(this, avatarN);
    	rotateLeftAction1 = new RotateLeftAction(avatarN);
    	rotateLeftAction2 = new RotateLeftAction(avatarN);
    	rotateRightAction1 = new RotateRightAction(avatarN);
    	rotateRightAction2 = new RotateRightAction(avatarN);
    	rotateYawAxisAction = new RotateYawAxisAction(rotateLeftAction2, rotateRightAction2);
    	throwGermAction = new ThrowGermAction(avatarN, this);
    	placeGermAction = new PlaceGermAction(avatarN, this);
    	quitGameAction = new QuitGameAction(this);
    	
    	ArrayList<Controller> controllersList = im.getControllers();
    	for (Controller keyboards : controllersList)
    	{
    		if (keyboards.getType() == Controller.Type.KEYBOARD)
    		{
    			
    			//Quit Game (KB: Escape)
    			im.associateAction(keyboards,
    						net.java.games.input.Component.Identifier.Key.ESCAPE,
    						quitGameAction,
    						InputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
    			
    			//Movement (KB: WASD)
    			im.associateAction(keyboards,
    						net.java.games.input.Component.Identifier.Key.W,
    						moveForwardPlayerOne,
    						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			
    			im.associateAction(keyboards,
    						net.java.games.input.Component.Identifier.Key.A,
    						rotateLeftAction1,
    						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			
    			im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.S,
						moveBackwardPlayerOne,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			
    			im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.D,
						rotateRightAction1,
						InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    			
    			im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.SPACE,
						throwGermAction,
						InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    			
    			im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.LCONTROL,
						placeGermAction,
						InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    			
    			im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.LSHIFT,
						germBoostAction,
						InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    			
    			im.associateAction(keyboards,
						net.java.games.input.Component.Identifier.Key.N,
						activateGermAction,
						InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    			
    		}
    	}
    	
    	if (isGamepadNull(gamepadName))
    	{
    		System.out.println("Could not find gamepad!");
    	}
    	else
    	{
    		im.associateAction(gamepadName,
    				net.java.games.input.Component.Identifier.Button._8,
    				quitGameAction,
    				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    		
    		im.associateAction(gamepadName,
    				net.java.games.input.Component.Identifier.Axis.Y,
    				moveYAxisAction,
    				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    		
    		im.associateAction(gamepadName,
    				net.java.games.input.Component.Identifier.Axis.X,
    				rotateYawAxisAction,
    				InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    		
    		im.associateAction(gamepadName,
    				net.java.games.input.Component.Identifier.Button._0,
    				throwGermAction,
    				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
    		
    		im.associateAction(gamepadName,
    				net.java.games.input.Component.Identifier.Button._1,
    				placeGermAction,
    				InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
 
    	}
    	
    }
    
    // Check if Gamepad is connected 
    private boolean isGamepadNull(String gamepadName)
    {
    	if (gamepadName == null)
    		return true;
    	else
    		return false;
    }
    
    // Increment Counter
    public void incrementCounter()
    {
    	counter++;;
    }

  
    private void incrementScore(String name)
    {
    	playerOneScore++;
	}
    
    public void syncAvatarPhysics(SceneNode player)
    {
    	if (active)
    	{
    		double[] transformation = player.getPhysicsObject().getTransform();
    		transformation[12] = player.getLocalPosition().x();
    		transformation[13] = player.getLocalPosition().y();
    		transformation[14] = player.getLocalPosition().z();
    		player.getPhysicsObject().setTransform(transformation);
    	}
    	else
    	{
    		player.getPhysicsObject().setTransform(createDoubleArray(player.getWorldTransform().toFloatArray()));
    	}
    }
    
    public double[] createDoubleArray(float[] array)
    {
    	if (array == null) return null;
    	
    	int size = array.length;
    	double[] newArray = new double[size];
    	for (int i = 0; i < size; i++)
    	{
    		newArray[i] = (double) array[i];
    	}
    	
    	return newArray;
    }
    
    public float[] createFloatArray(double[] array)
    {
    	if (array == null) return null;
    	
    	int size = array.length;
    	float[] newArray = new float[size];
    	for (int i = 0; i < size; i++)
    	{
    		newArray[i] = (float) array[i];
    	}
    	
    	return newArray;
    }
    
    public void updateVerticalPosition() 
    {
    	
    	SceneNode avatarN = this.getEngine().getSceneManager().getSceneNode("avatarNode");
    	SceneNode tessN = this.getEngine().getSceneManager().getSceneNode("tessellationNode");
    	
    	Tessellation tessellationE = ((Tessellation) tessellationN.getAttachedObject("tessellationEntity"));
    	
		Vector3 avatarWorldPositionP1 = avatarN.getWorldPosition();
		Vector3 avatarLocalPositionP1 = avatarN.getLocalPosition();
		
		Vector3 terrainPositionP1 = (Vector3) Vector3f.createFrom(avatarLocalPositionP1.x(),
				tessellationE.getWorldHeight(avatarWorldPositionP1.x(), avatarWorldPositionP1.z()),
				avatarLocalPositionP1.z());
		
		avatarN.setLocalPosition(terrainPositionP1);
	}
	
    
    // ***************************************************************
    // **********************PHYSICS SECTION**************************
    //****************************************************************
    
    private void setupPhysics()
    {
    	String engineString = "ray.physics.JBullet.JBulletPhysicsEngine";
    	
    	//Create Gravity
    	float[] gravity = { 0.0f, -9.8f, 0.0f };
    	
    	physicsEng = PhysicsEngineFactory.createPhysicsEngine(engineString);
    	physicsEng.initSystem();
    	physicsEng.setGravity(gravity);
    }
    
    private void setupPhysicsWorld()
    {
    	float directionUp[] = { 0.0f, 1f, 0.0f };
    	float objMass = 1.0f;
    	double[] transformation, planeTransformation;
    	
    	// Dolphin One Settings
    	transformation = createDoubleArray(avatarN.getLocalTransform().toFloatArray());
    	avatarPhysObj = physicsEng.addCapsuleObject(physicsEng.nextUID(), objMass, transformation, 0.2f, 1.0f);
    	avatarPhysObj.setFriction(0.0f);
    	avatarPhysObj.setDamping(0.9f, 0.9f);
    	avatarPhysObj.setBounciness(0.0f);
    	avatarPhysObj.setSleepThresholds(0.0f, 0.0f);
    	avatarN.setPhysicsObject(avatarPhysObj);
    	

    	// Floor/Ground Settings
    	transformation = createDoubleArray(groundN.getLocalTransform().toFloatArray());
    	groundPhysObj = physicsEng.addStaticPlaneObject(physicsEng.nextUID(), transformation, directionUp, 0.0f);
    	groundPhysObj.setBounciness(0.25f);
    	
    	groundN.scale(100.0f, 1.0f, 100.0f);
    	groundN.setLocalPosition(0.0f, 0.0f, 0.0f);
    	planeTransformation = groundPhysObj.getTransform();
    	planeTransformation[12] = groundN.getLocalPosition().x();
    	planeTransformation[13] = groundN.getLocalPosition().y();
    	planeTransformation[14] = groundN.getLocalPosition().z();
    	groundPhysObj.setTransform(planeTransformation);
    	groundN.setPhysicsObject(groundPhysObj);
    	
    }
    
    public void throwGerm()
    {
    	try
    	{
 
    		int uid = physicsEng.nextUID();
    		float mass = 10.0f;
    		germballE = getEngine().getSceneManager().createEntity("germball" + uid,  "germball.obj");
    		germballN = getEngine().getSceneManager().getRootSceneNode().createChildSceneNode("germball" + uid);
    		germballN.scale(.5f, .5f, .5f);
    		germballN.attachObject(germballE);
    		germNodes.add(germballN);
    		
    		// Current Set Location: Sometimes boosts the player
    		// **HOWEVER, if I change the XYZ pos by +-2.0f then the germ is thrown off center of the character..**
    		// So im choosing this route for now.. :)
    		//germballN.setLocalPosition(getPlayerPos());
    		germballN.setLocalPosition(getPlayerPos().x() + 2.0f, getPlayerPos().y(), getPlayerPos().z() + 1.0f);
    		
    		
    		germballN.setLocalRotation(avatarN.getWorldRotation());	
    		double[] transformation = createDoubleArray(avatarN.getLocalTransform().toFloatArray());
        	PhysicsObject germball = physicsEng.addCapsuleObject(physicsEng.nextUID(), mass, transformation, 0.2f, 1.0f);
    		Vector3 a = germballN.getLocalRotation().mult(Vector3f.createFrom(0, 0, 10));
    		germball.setLinearVelocity(new float[] {
    												a.x(),
    												a.y(),
    												a.z()
    												});
    		germball.applyForce(100, 100, 100, 50, 50, 50);
    		germball.setDamping(.55f, 0f);
    		germballN.setPhysicsObject(germball);
    	
    	} catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	coughSound.play();
    	doTheCough();
    	doTheWalk();
    }
    
    private void doTheCough()
    {
    	SkeletalEntity manSE = (SkeletalEntity) getEngine().getSceneManager().getEntity("avatar");
    	manSE.stopAnimation();
    	manSE.playAnimation("coughAction", 0.5f, SkeletalEntity.EndType.NONE, 0);
    }
    
    public void placeGerm()
    {
    	try
    	{
 
    		int uid = physicsEng.nextUID();
    		float mass = 10.0f;
    		Entity germballE = getEngine().getSceneManager().createEntity("germball" + uid,  "germball.obj");
    		SceneNode germballN = getEngine().getSceneManager().getRootSceneNode().createChildSceneNode("germball" + uid);
    		germNodes.add(germballN);
    		germballN.scale(.5f, .5f, .5f);
    		germballN.attachObject(germballE);
    		germballN.setLocalPosition(getPlayerPos().x(), getPlayerPos().y() -0.7f, getPlayerPos().z() + 1.0f);
    		germballN.setLocalRotation(avatarN.getWorldRotation());	
//    		germballN.moveLeft(.1f);
//    		germballN.moveUp(.05f);
    		double[] floats = JBulletUtils.float_to_double_array(germballN.getLocalTransform().toFloatArray());
    		PhysicsObject germball = physicsEng.addSphereObject(uid,  mass,  null, 2.0f);
    		germballN.setPhysicsObject(germball);
    	} catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	clapSound.play();
    	doTheClap();
    }
    
    private void doTheClap()
    {
    	SkeletalEntity manSE = (SkeletalEntity) getEngine().getSceneManager().getEntity("avatar");
    	manSE.stopAnimation();
    	manSE.playAnimation("clapAction", 0.5f, SkeletalEntity.EndType.NONE, 0);
    }
    
    private void doTheWalk()
    {
    	SkeletalEntity manSE = (SkeletalEntity) getEngine().getSceneManager().getEntity("npc1");
    	manSE.stopAnimation();
    	manSE.playAnimation("walkAction", 0.5f, SkeletalEntity.EndType.LOOP, 0);
    }
    
    private void doTheBlade()
    {
    	SkeletalEntity manSE = (SkeletalEntity) getEngine().getSceneManager().getEntity("npc1");
    	manSE.stopAnimation();
    	manSE.playAnimation("bladeAction", 0.5f, SkeletalEntity.EndType.LOOP, 0);
    }
    
    
    public void germBoost()
    {
    	try
    	{
 
    		int uid = physicsEng.nextUID();
    		float mass = 1.0f;
    		Entity germballE = getEngine().getSceneManager().createEntity("germball" + uid,  "germball.obj");
    		SceneNode germballN = getEngine().getSceneManager().getRootSceneNode().createChildSceneNode("germball" + uid);
    		germNodes.add(germballN);
    		germballN.scale(.2f, .2f, .2f);
    		germballN.attachObject(germballE);
    		germballN.setLocalPosition(getPlayerPos().x(), getPlayerPos().y() -1.5f, getPlayerPos().z());
    		germballN.setLocalRotation(avatarN.getWorldRotation());	
    		double[] transformation = createDoubleArray(avatarN.getLocalTransform().toFloatArray());
        	PhysicsObject germball = physicsEng.addCapsuleObject(physicsEng.nextUID(), mass, transformation, 0.2f, 1.0f);
    		Vector3 a = germballN.getLocalRotation().mult(Vector3f.createFrom(10, 10, 50));
    		germball.setLinearVelocity(new float[] {
    												a.x(),
    												a.y(),
    												a.z()
    												});
    		germball.setDamping(.55f, 0f);
    		germballN.setPhysicsObject(germball);
    	} catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    // Update must redraw the scene while running the keyboard count + time
    @Override
    protected void update(Engine engine)
    {
    	rs = (GL4RenderSystem) engine.getRenderSystem();
    	elapsTime += engine.getElapsedTimeMillis();
    	
    	if (active)
    	{
    		Matrix4 matrix;
    		physicsEng.update(elapsTime);
    		for (SceneNode sceneNode: engine.getSceneManager().getSceneNodes())
    		{
    			if (sceneNode.getPhysicsObject() != null)
    			{
    			matrix = Matrix4f.createFrom(createFloatArray(sceneNode.getPhysicsObject().getTransform()));
    			sceneNode.setLocalPosition(matrix.value(0, 3), matrix.value(1, 3), matrix.value(2, 3));
    			}
    		}
    	}
    	
    	elapsTimeSec = Math.round(elapsTime / 1000.0f);
    	elapsTimeStr = Integer.toString(elapsTimeSec);
    	playerOneScoreStr = Integer.toString(playerOneScore);
    	playerOneLivesStr = Integer.toString(playerOneLives);
    	
    	dispStr1 = "Player One Time = " + elapsTimeStr;
    	dispStr1 += " | Lives: " + playerOneLivesStr;
    	dispStr1 += " | Score: " + playerOneScoreStr;
    	dispStr1 += " | Carrying Vaccine: " + hasVaccine();
    	
    	rs.setHUD(dispStr1, 15, (rs.getRenderWindow().getViewport(0).getActualBottom() + 2));
    	
    	im.update(elapsTime);
    	orbitCamOne.updateCameraPos();
    	coughSound.setLocation(avatarN.getWorldPosition());
    	clapSound.setLocation(avatarN.getWorldPosition());
    	setEarParameters(sm);
    	redFlagCollisionDetection(avatarN);
    	blueFlagCollisionDetection(avatarN);
    	greenFlagCollisionDetection(avatarN);
    	OrangeFlagCollisionDetection(avatarN);
    	germCollisionDetection(avatarN);
    	checkWin();
    	checkLose();
    	try {
			hospitalCollisionDetection(avatarN);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	avatarE.update();
    	npc1E.update();
    	//processNetworking(elapsTime);
    	//updateVerticalPosition();
    	
    }
    
    public Vector3 getPlayerPos()
    {
    	SceneNode avatarN = sm.getSceneNode("avatarNode");
    	return avatarN.getWorldPosition();
    }
    
    public void initAudio(SceneManager sm)
    {
    	AudioResource resource1, resource2, resource3;
    	audioMgr = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
   
    	if (!audioMgr.initialize())
    	{
    		System.out.println("Audio Manager failed to initialize!");
    		return;
    	}
 
    	resource1 = audioMgr.createAudioResource("cough.wav", AudioResourceType.AUDIO_SAMPLE);
    	resource2 = audioMgr.createAudioResource("clap.wav", AudioResourceType.AUDIO_SAMPLE);
    	resource3 = audioMgr.createAudioResource("background.wav", AudioResourceType.AUDIO_STREAM);
    	
    	coughSound = new Sound(resource1, SoundType.SOUND_EFFECT, 100, false);
    	clapSound = new Sound(resource2, SoundType.SOUND_EFFECT, 100, false);
    	bgSound = new Sound(resource3, SoundType.SOUND_EFFECT, 100, true);
    	
    	//Background Sound
    	bgSound.initialize(audioMgr);
    	bgSound.setMaxDistance(1000.0f);
    	bgSound.setMinDistance(0.5f);
    	bgSound.setRollOff(5.0f);
    	
    	//Cough Sound
    	coughSound.initialize(audioMgr);
    	coughSound.setMaxDistance(10.0f);
    	coughSound.setMinDistance(0.5f);
    	coughSound.setRollOff(5.0f);
    	
    	//Clap Sound
    	clapSound.initialize(audioMgr);
    	clapSound.setMaxDistance(10.0f);
    	clapSound.setMinDistance(0.5f);
    	clapSound.setRollOff(5.0f);
    	
    	SceneNode avatarN = sm.getSceneNode("avatarNode");
    	coughSound.setLocation(avatarN.getWorldPosition());
    	clapSound.setLocation(avatarN.getWorldPosition());
    	bgSound.setLocation(avatarN.getWorldPosition());
    	
    	setEarParameters(sm);
    	bgSound.play();
    
    	
    	
    	
    }
    
    public void setEarParameters(SceneManager sm)
    {
    	SceneNode cameraN = sm.getSceneNode("cameraOneNode");
    	Vector3 avDir = avatarN.getWorldForwardAxis();
    	
    	audioMgr.getEar().setLocation(cameraN.getWorldPosition());
    	audioMgr.getEar().setOrientation(avDir, Vector3f.createFrom(0,1,0));
    }
    
    //**************************************************************************************************************//
    //**********************             NETWORKING                                             ********************//
    //**************************************************************************************************************//
    
//    protected void setupNetworking() throws IOException
//    {
//    	gameObjectsToRemove = new Vector<UUID>();
//    	isClientConnected = false;
//    	
//    	try
//    	{
//    		protocolClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
//    	} catch (UnknownHostException e)
//    	{
//    		e.printStackTrace();
//    	}
//    	
//    	if (protocolClient == null)
//    	{
//    		System.out.println("Missing protocol host.");
//    	}
//    	else
//    	{
//    		protocolClient.sendJoinMessage();
//    	}
//    }
//    
//    public void processNetworking(float elapsTime)
//    {
//    	if (protocolClient != null)
//    	{
//    		protocolClient.processPackets();
//    	}
//    	
//    	if (gameObjectsToRemove != null)
//    	{
//    	Iterator<UUID> iter = gameObjectsToRemove.iterator();
//    	
//    	while (iter.hasNext())
//    	{
//    		sm.destroySceneNode(iter.next().toString());
//    	}
//    	gameObjectsToRemove.clear();
//    	}
//    }
//
//	public void setIsConnected(boolean b) 
//	{
//		isClientConnected = b;
//	}
//	
//	private class SendCloseConnectionPacketAction extends AbstractInputAction
//	{
//		@Override
//		public void performAction(float time, Event evt)
//		{
//			if (protocolClient != null && isClientConnected == true)
//			{
//				protocolClient.sendByeMessage();
//			}
//		}
//	}
//	
//	//*****************************//
//	//******* Ghost Avatars *******//
//	//*****************************//
//	
//	public void addGhostAvatarToGameWorld(GhostAvatar avatar) throws IOException
//	{
//		if (avatar != null)
//		{
//			Entity ghostE = sm.createEntity(avatar.getID().toString(), "dolphinHighPoly.obj");
//			ghostE.setPrimitive(Primitive.TRIANGLES);
//			SceneNode ghostN = sm.getRootSceneNode().createChildSceneNode(avatar.getID().toString());
//			ghostN.attachObject(ghostE);
//			ghostN.setLocalPosition(500, 0, 100);
//			avatar.setEntity(ghostE);
//			avatar.setPos(ghostN.getLocalPosition());
//		}
//	}
//	
//	public void removeGhostAvatarFromGameWorld(GhostAvatar avatar)
//	{
//		if (avatar != null)
//		{
//			gameObjectsToRemove.add(avatar.getID());
//		}
//	}
//	
//	public void moveGhostAvatarAroundGameWorld(GhostAvatar avatar, Vector3 pos)
//	{
//		avatar.getSceneNode().setLocalPosition(pos);
//	}
//	
//	//*****************************//
//	//**** Collision Detection*****//
//	//*****************************//
    
    private void redFlagCollisionDetection(SceneNode player)
    {
    	Vector3f playerPos = (Vector3f) player.getWorldPosition();
    	Vector3f redFlagPos = (Vector3f) redFlagN.getWorldPosition();
    	
    	if (playerHasRedFlag == false && playerHasBlueFlag == false && playerHasGreenFlag == false && playerHasOrangeFlag == false)
    	{
    	if ((Math.pow((playerPos.x() - redFlagPos.x()), 2) + Math.pow((playerPos.y() - redFlagPos.y()), 2) + Math.pow((playerPos.z() - redFlagPos.z()), 2)) < Math.pow((2.15f), 2.0f))
    	{
    		avatarN.attachChild(redFlagN);
    		redFlagN.setLocalPosition(avatarN.getLocalPosition().x() - 50f, avatarN.getLocalPosition().y() + 3.5f, avatarN.getLocalPosition().z() - 48.0f);
    		playerHasRedFlag = true;
    	}
    	}
    }
    
    private void blueFlagCollisionDetection(SceneNode player)
    {
    	Vector3f playerPos = (Vector3f) player.getWorldPosition();
    	Vector3f blueFlagPos = (Vector3f) blueFlagN.getWorldPosition();
    	
    	if (playerHasBlueFlag == false && playerHasRedFlag == false && playerHasGreenFlag == false && playerHasOrangeFlag == false)
    	{
    		if ((Math.pow((playerPos.x() - blueFlagPos.x()), 2) + Math.pow((playerPos.y() - blueFlagPos.y()), 2) + Math.pow((playerPos.z() - blueFlagPos.z()), 2)) < Math.pow((2.15f), 2.0f))
    		{
    			avatarN.attachChild(blueFlagN);
    			blueFlagN.setLocalPosition(avatarN.getWorldPosition());
    			blueFlagN.moveRight(50.0f);
    			blueFlagN.moveForward(49.0f);
    			playerHasBlueFlag = true;
    		}
    	}
    }
    
    private void greenFlagCollisionDetection(SceneNode player)
    {
    	Vector3f playerPos = (Vector3f) player.getWorldPosition();
    	Vector3f greenFlagPos = (Vector3f) greenFlagN.getWorldPosition();
    	
    	if (playerHasBlueFlag == false && playerHasRedFlag == false && playerHasGreenFlag == false && playerHasOrangeFlag == false)
    	{
    		if ((Math.pow((playerPos.x() - greenFlagPos.x()), 2) + Math.pow((playerPos.y() - greenFlagPos.y()), 2) + Math.pow((playerPos.z() - greenFlagPos.z()), 2)) < Math.pow((2.15f), 2.0f))
    		{
    		avatarN.attachChild(greenFlagN);
    		greenFlagN.setLocalPosition(avatarN.getLocalPosition());
    		greenFlagN.moveRight(49.2f);
    		greenFlagN.moveBackward(58.5f);
    		greenFlagN.moveUp(1.0f);
    		playerHasGreenFlag = true;
    		}
    	}
    }
    
    private void OrangeFlagCollisionDetection(SceneNode player)
    {
    	Vector3f playerPos = (Vector3f) player.getWorldPosition();
    	Vector3f orangeFlagPos = (Vector3f) orangeFlagN.getWorldPosition();
    	
    	if (playerHasBlueFlag == false && playerHasRedFlag == false && playerHasGreenFlag == false && playerHasOrangeFlag == false)
    	{
    		if ((Math.pow((playerPos.x() - orangeFlagPos.x()), 2) + Math.pow((playerPos.y() - orangeFlagPos.y()), 2) + Math.pow((playerPos.z() - orangeFlagPos.z()), 2)) < Math.pow((2.15f), 2.0f))
    		{
    		avatarN.attachChild(orangeFlagN);
    		orangeFlagN.setLocalPosition(avatarN.getLocalPosition().x() - 87f, avatarN.getLocalPosition().y() + 1.7f, avatarN.getLocalPosition().z() + 50.5f);
    		playerHasOrangeFlag = true;
    		}
    	}
    }
    
    public void germCollisionDetection(SceneNode player)
    {
    	float[] dead = {0, 0, 0};
    	
    	if(germActive == true)
    	{
    		Vector3f playerPos = (Vector3f) player.getWorldPosition();
    	
    		germNodes.forEach((n) ->
    		{
    			Vector3f germPos = (Vector3f) n.getWorldPosition();
    			{
    				if ((Math.pow((playerPos.x() - germPos.x()), 2) + Math.pow((playerPos.y() - germPos.y()), 2) + Math.pow((playerPos.z() - germPos.z()), 2)) < Math.pow((2.15f), 2.0f))
    				{
    					player.setLocalPosition(51.58f, 1.0f, 0.17f);
    					orbitCamOne.setCameraPos(48.58f, 5.0f, 0.17f);
    					playerOneLives--;
    				}
    			}
    		});
    		syncAvatarPhysics(avatarN);
    	}
    }
    
    public void activateGerm()
    {
    	germActive = !germActive;
    	System.out.println(germActive);
    	doTheWalk();
    	
    }
    	
    private void hospitalCollisionDetection(SceneNode player) throws IOException
    {
    	// Spawn Locations
    	// redSpawn = {51.23f, 0f, 49.14f};
    	// blueSpawn = {-49.15f, -1.0f, -49.91f};
    	// greenSpawn = {-49.17f, -1.0f, 56.85f};
    	// randomSpawn1 = {20f, -1.0f, 20f};
    	
    	Vector3f playerPos = (Vector3f) player.getWorldPosition();
    	//Vector3f hospitalPos = 1.0634489, 0.19984622, -0.06711626
    	if ((Math.pow((playerPos.x() - 1.0634489), 2) + Math.pow((playerPos.y() - 0.19984622), 2) + Math.pow((playerPos.z() + 0.06711626), 2)) < Math.pow((2.15f), 2.0f))
    	{
    		if (playerHasRedFlag == true)
    		{
    			scoreRed();
    			deleteRedFlag();
    			createRedFlag(51.23f, 0f, 49.14f);
    			playerHasRedFlag = false;	
    		}
    		else if (playerHasBlueFlag == true)
    		{
    			scoreBlue();
    			deleteBlueFlag();
    			createBlueFlag(-49.15f, -1.0f, -49.91f);
    			playerHasBlueFlag = false;
    		}
    		else if (playerHasGreenFlag == true)
    		{
    			scoreGreen();
    			deleteGreenFlag();
    			createGreenFlag(-49.17f, -1.0f, 56.85f);
    			playerHasGreenFlag = false;
    		}
    		else if (playerHasOrangeFlag == true)
    		{
    			scoreOrange();
    			deleteOrangeFlag();
    			createOrangeFlag(89.1f, 0f, -51.49f);
    			playerHasOrangeFlag = false;
    		}
    	}
    }
    
    
    // Scoring
    
    public void scoreRed()
    {
    	playerOneScore = playerOneScore + 100;
    }
    
    public void scoreBlue()
    {
    	playerOneScore = playerOneScore + 250;
    }
    
    public void scoreGreen()
    {
    	playerOneScore = playerOneScore + 500;
    
    }
    
    public void scoreOrange()
    {
    	playerOneScore = playerOneScore + 750;
    }
    
    //Create and Destroy Vaccines
    //Red Flag
    
    public void createRedFlag() throws IOException
    {
    	redFlagE = sm.createEntity("redflag", "syringe.obj");
    	redFlagE.setPrimitive(Primitive.TRIANGLES);
    	redFlagN = sm.getRootSceneNode().createChildSceneNode("redFlagNode");
    	redFlagN.scale(.8f,.5f,.8f);
    	redFlagN.attachObject(redFlagE);
    	redFlagN.moveLeft(1.0f);
    	
    	redFlagN.setLocalPosition(51.23f, 0f, 49.13f);
    }
    
    public void createRedFlag(float x, float y, float z, boolean noRandom) throws IOException
    {
    	if (noRandom == true)
    	{
    	redFlagE = sm.createEntity("redflag", "syringe.obj");
    	redFlagE.setPrimitive(Primitive.TRIANGLES);
    	redFlagN = sm.getRootSceneNode().createChildSceneNode("redFlagNode");
    	redFlagN.scale(.8f,.5f,.8f);
    	redFlagN.attachObject(redFlagE);
    	redFlagN.moveLeft(1.0f);
    	
    	redFlagN.setLocalPosition(x, y, z);
    	}
    }
    public void createRedFlag(float x, float y, float z) throws IOException
    {
    	redFlagE = sm.createEntity("redflag", "syringe.obj");
    	redFlagE.setPrimitive(Primitive.TRIANGLES);
    	redFlagN = sm.getRootSceneNode().createChildSceneNode("redFlagNode");
    	redFlagN.scale(.8f,.5f,.8f);
    	redFlagN.attachObject(redFlagE);
    	redFlagN.moveLeft(1.0f);
    	
    	int randomNum = rand.nextInt(3);
    	float[] randomSpawn = getRandomSpawn(randomNum);
    	
    	if (randomNum == 0)
    	{
    		deleteRedFlag();
    		createRedFlag();
    	}
    	if (randomNum == 1)
    	{
    		deleteBlueFlag();
    		createBlueFlag(51.23f, 0f, 49.13f, true);
    		deleteRedFlag();
    		createRedFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 2)
    	{
    		deleteGreenFlag();
    		createGreenFlag(51.23f, 0f, 49.13f, true);
    		deleteRedFlag();
    		createRedFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 3)
    	{
    		deleteOrangeFlag();
    		createOrangeFlag(51.23f, 0f, 49.13f, true);
    		deleteRedFlag();
    		createRedFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	
    }
    
    public void deleteRedFlag()
    {
    	avatarN.detachChild(redFlagN);
	    sm.getRootSceneNode().detachChild(redFlagN);
		sm.destroySceneNode(redFlagN);
		sm.destroyEntity(redFlagE);
    }
    
    //Green Flag
    
    public void createGreenFlag() throws IOException
    {
    	greenFlagE = sm.createEntity("greenflag", "syringe.obj");
        greenFlagE.setPrimitive(Primitive.TRIANGLES);
        
        Texture greenFlagT = tm.getAssetByPath("greensyringe.jpeg");
        TextureState greenFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        greenFlagTS.setTexture(greenFlagT);
        greenFlagE.setRenderState(greenFlagTS);
       
        greenFlagN = sm.getRootSceneNode().createChildSceneNode("greenFlagNode");
        greenFlagN.scale(.8f,.8f,.8f);
        greenFlagN.attachObject(greenFlagE);
        greenFlagN.moveLeft(1.0f);
        
        greenFlagN.setLocalPosition(-49.17f, -1.0f, 56.85f);
    }
    
    public void createGreenFlag(float x, float y, float z, boolean noRandom) throws IOException
    {
    	if (noRandom == true)
    	{
    		greenFlagE = sm.createEntity("greenflag", "syringe.obj");
            greenFlagE.setPrimitive(Primitive.TRIANGLES);
            
            Texture greenFlagT = tm.getAssetByPath("greensyringe.jpeg");
            TextureState greenFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            greenFlagTS.setTexture(greenFlagT);
            greenFlagE.setRenderState(greenFlagTS);
           
            greenFlagN = sm.getRootSceneNode().createChildSceneNode("greenFlagNode");
            greenFlagN.scale(.8f,.8f,.8f);
            greenFlagN.attachObject(greenFlagE);
            greenFlagN.moveLeft(1.0f);
            
            greenFlagN.setLocalPosition(x, y, z);
    	}
    }
    
    public void createGreenFlag(float x, float y, float z) throws IOException
    {
    	greenFlagE = sm.createEntity("greenflag", "syringe.obj");
        greenFlagE.setPrimitive(Primitive.TRIANGLES);
        
        Texture greenFlagT = tm.getAssetByPath("greensyringe.jpeg");
        TextureState greenFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        greenFlagTS.setTexture(greenFlagT);
        greenFlagE.setRenderState(greenFlagTS);
       
        greenFlagN = sm.getRootSceneNode().createChildSceneNode("greenFlagNode");
        greenFlagN.scale(.8f,.8f,.8f);
        greenFlagN.attachObject(greenFlagE);
        greenFlagN.moveLeft(1.0f);
        
        int randomNum = rand.nextInt(3);
    	float[] randomSpawn = getRandomSpawn(randomNum);
    	
    	if (randomNum == 0)
    	{
    		deleteRedFlag();
    		createRedFlag(-49.17f, -1.0f, 56.85f, true);
    		deleteGreenFlag();
    		createGreenFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 1)
    	{
    		deleteBlueFlag();
    		createBlueFlag(-49.17f, -1.0f, 56.85f, true);
    		deleteGreenFlag();
    		createGreenFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 2)
    	{
    		deleteGreenFlag();
    		createGreenFlag();
    	}
    	if (randomNum == 3)
    	{
    		deleteOrangeFlag();
    		createOrangeFlag(-49.17f, -1.0f, 56.85f, true);
    		deleteGreenFlag();
    		createGreenFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
        
    }
    
    public void deleteGreenFlag()
    {
    	avatarN.detachChild(greenFlagN);
	    sm.getRootSceneNode().detachChild(greenFlagN);
		sm.destroySceneNode(greenFlagN);
		sm.destroyEntity(greenFlagE);
    }
    
    //Blue Flag
    
    public void createBlueFlag() throws IOException
    {
    	Entity blueFlagE = sm.createEntity("blueflag", "syringe.obj");
        blueFlagE.setPrimitive(Primitive.TRIANGLES);
         
        Texture blueFlagT = tm.getAssetByPath("bluesyringe.jpeg");
        TextureState blueFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        blueFlagTS.setTexture(blueFlagT);
        blueFlagE.setRenderState(blueFlagTS);
         
         //Create the Node and place the flag
        blueFlagN = sm.getRootSceneNode().createChildSceneNode("blueFlagNode");
        blueFlagN.scale(.8f,.8f,.8f);
        blueFlagN.attachObject(blueFlagE);
        blueFlagN.moveLeft(1.0f);
        
        blueFlagN.setLocalPosition(-49.16f, -1.0f, -49.91f);
    }
    
    public void createBlueFlag(float x, float y, float z, boolean noRandom) throws IOException
    {
    	if (noRandom == true)
    	{
    		blueFlagE = sm.createEntity("blueflag", "syringe.obj");
            blueFlagE.setPrimitive(Primitive.TRIANGLES);
             
            Texture blueFlagT = tm.getAssetByPath("bluesyringe.jpeg");
            TextureState blueFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            blueFlagTS.setTexture(blueFlagT);
            blueFlagE.setRenderState(blueFlagTS);
             
             //Create the Node and place the flag
            blueFlagN = sm.getRootSceneNode().createChildSceneNode("blueFlagNode");
            blueFlagN.scale(.8f,.8f,.8f);
            blueFlagN.attachObject(blueFlagE);
            blueFlagN.moveLeft(1.0f);
            
            blueFlagN.setLocalPosition(x, y, z);
    	}
    }
    
    public void createBlueFlag(float x, float y, float z) throws IOException
    {
    	blueFlagE = sm.createEntity("blueflag", "syringe.obj");
        blueFlagE.setPrimitive(Primitive.TRIANGLES);
         
        Texture blueFlagT = tm.getAssetByPath("bluesyringe.jpeg");
        TextureState blueFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        blueFlagTS.setTexture(blueFlagT);
        blueFlagE.setRenderState(blueFlagTS);
         
         //Create the Node and place the flag
        blueFlagN = sm.getRootSceneNode().createChildSceneNode("blueFlagNode");
        blueFlagN.scale(.8f,.8f,.8f);
        blueFlagN.attachObject(blueFlagE);
        blueFlagN.moveLeft(1.0f);
        
        int randomNum = rand.nextInt(3);
    	float[] randomSpawn = getRandomSpawn(randomNum);
    	
    	if (randomNum == 0)
    	{
    		deleteRedFlag();
    		createRedFlag(-49.16f, -1.0f, -49.91f, true);
    		deleteBlueFlag();
    		createBlueFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 1)
    	{
    		deleteBlueFlag();
    		createBlueFlag();
    	}
    	if (randomNum == 2)
    	{
    		deleteGreenFlag();
    		createGreenFlag(-49.16f, -1.0f, -49.91f);
    		deleteBlueFlag();
    		createBlueFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 3)
    	{
    		deleteOrangeFlag();
    		createOrangeFlag(-49.16f, -1.0f, -49.91f);
    		deleteBlueFlag();
    		createBlueFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	
       
    }
    
    public void deleteBlueFlag()
    {
    	avatarN.detachChild(blueFlagN);
	    sm.getRootSceneNode().detachChild(blueFlagN);
		sm.destroySceneNode(blueFlagN);
		sm.destroyEntity("blueflag");
    }
    
    public void createOrangeFlag() throws IOException
    {
    	// Set Orange Flag Texture (default flag.mtl has redflag.jpeg as default)
        orangeFlagE = sm.createEntity("orangeflag", "syringe.obj");
        orangeFlagE.setPrimitive(Primitive.TRIANGLES);
        
        Texture orangeFlagT = tm.getAssetByPath("orangesyringe.jpeg");
        TextureState orangeFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        orangeFlagTS.setTexture(orangeFlagT);
        orangeFlagE.setRenderState(orangeFlagTS);
       
        orangeFlagN = sm.getRootSceneNode().createChildSceneNode("orangeFlagNode");
        orangeFlagN.scale(.8f,.8f,.8f);
        orangeFlagN.attachObject(orangeFlagE);
        orangeFlagN.moveLeft(1.0f);
        
        orangeFlagN.setLocalPosition(89f, 0.0f, -51.3f);
    }
    
    public void createOrangeFlag(float x, float y, float z, boolean noRandom) throws IOException
    {
    	if (noRandom == true)
    	{
    		// Set Orange Flag Texture (default flag.mtl has redflag.jpeg as default)
            orangeFlagE = sm.createEntity("orangeflag", "syringe.obj");
            orangeFlagE.setPrimitive(Primitive.TRIANGLES);
            
            Texture orangeFlagT = tm.getAssetByPath("orangesyringe.jpeg");
            TextureState orangeFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
            orangeFlagTS.setTexture(orangeFlagT);
            orangeFlagE.setRenderState(orangeFlagTS);
           
            //Orange Flag
            orangeFlagN = sm.getRootSceneNode().createChildSceneNode("orangeFlagNode");
            orangeFlagN.scale(.8f,.8f,.8f);
            orangeFlagN.attachObject(orangeFlagE);
            orangeFlagN.moveLeft(1.0f);
            
            orangeFlagN.setLocalPosition(x, y, z);
    	}
    }
    
    public void createOrangeFlag(float x, float y, float z) throws IOException
    {
    	// Set Orange Flag Texture (default flag.mtl has redflag.jpeg as default)
        orangeFlagE = sm.createEntity("orangeflag", "syringe.obj");
        orangeFlagE.setPrimitive(Primitive.TRIANGLES);
        
        Texture orangeFlagT = tm.getAssetByPath("orangesyringe.jpeg");
        TextureState orangeFlagTS = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        orangeFlagTS.setTexture(orangeFlagT);
        orangeFlagE.setRenderState(orangeFlagTS);
       
        //Orange Flag
        orangeFlagN = sm.getRootSceneNode().createChildSceneNode("orangeFlagNode");
        orangeFlagN.scale(.8f,.8f,.8f);
        orangeFlagN.attachObject(orangeFlagE);
        
        int randomNum = rand.nextInt(3);
    	float[] randomSpawn = getRandomSpawn(randomNum);
    	
    	if (randomNum == 0)
    	{
    		deleteRedFlag();
    		createRedFlag(89f, 0.0f, -51.3f, true);
    		deleteOrangeFlag();
    		createOrangeFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 1)
    	{
    		deleteBlueFlag();
    		createBlueFlag(89f, 0.0f, -51.3f, true);
    		deleteOrangeFlag();
    		createOrangeFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 2)
    	{
    		deleteGreenFlag();
    		createGreenFlag(89f, 0.0f, -51.3f, true);
    		deleteOrangeFlag();
    		createOrangeFlag(randomSpawn[0], randomSpawn[1], randomSpawn[2], true);
    	}
    	if (randomNum == 3)
    	{
    		deleteOrangeFlag();
    		createOrangeFlag();
    	}
        
    }
    
    public void deleteOrangeFlag()
    {
    	avatarN.detachChild(orangeFlagN);
	    sm.getRootSceneNode().detachChild(orangeFlagN);
		sm.destroySceneNode(orangeFlagN);
		sm.destroyEntity(orangeFlagE);
    }
    
    public float[] getRandomSpawn(int randomNum)
    {
    	// Spawn Locations
    	// redSpawn = {51.23f, 0f, 49.14f};
    	// blueSpawn = {-49.15f, -1.0f, -49.91f};
    	//] greenSpawn = {-49.17f, -1.0f, 56.85f};
    	// randomSpawn1 = {20f, -1.0f, 20f};
    	
    	float[] spawn = {0,0,0};
   
    	if (randomNum == 0)
    	{
    		spawn[0] = 51.23f;
    		spawn[1] = 0f;
    		spawn[2] = 49.14f;
    	}
    	else if (randomNum == 1)
    	{
    		spawn[0] = -49.15f;
    		spawn[1] = 0f;
    		spawn[2] = -49.91f;
    	}
    	else if (randomNum == 2)
    	{
    		spawn[0] = -49.17f;
    		spawn[1] = 0f;
    		spawn[2] = 56.85f;
    	}
    	else if (randomNum == 3)
    	{
    		spawn[0] = 89f;
    		spawn[1] = 0f;
    		spawn[2] = -51.3f;
    	}
    	
    	return spawn;
    }
    
    public boolean hasVaccine()
    {
    	if (playerHasRedFlag == true || playerHasBlueFlag == true || playerHasGreenFlag == true || playerHasOrangeFlag == true)
    	{
    		return true;
    	}
    	else return false;
    }
    
    public float getElapsedTime()
    {
    	return elapsTime;
    }
    
    
	private void runScript(ScriptEngine engine, File scriptFile)
	{
		try    
		{ 
			FileReader fileReader = new FileReader(scriptFile);
			engine.eval(fileReader);
			fileReader.close();    
		}
		catch(FileNotFoundException e1) {
			System.out.println(scriptFile + " not found " + e1); 
		} catch (IOException e2)     
		{ 
			System.out.println("IO problem with " + scriptFile + e2); 
		}catch (ScriptException e3)      
		{ 
			System.out.println("ScriptException in " + scriptFile + e3); 
		}catch (NullPointerException e4)   
		{ 
			System.out.println ("Null ptr exception in " + scriptFile + e4); 
		}
	}
	
	public void checkWin()
	{
		if (playerOneScore > 10000)
		{
			JOptionPane.showMessageDialog(null, "You did it! You've collected enough vaccines to take one yourself");
			this.shutdown();
			this.exit();
		}
	}
	
	public void checkLose()
	{
		if (playerOneLives <= 0)
		{
			JOptionPane.showMessageDialog(null, "You Lost! You've been Diagnosed with Covid-19 :(");
			this.shutdown();
			this.exit();
		}
	}
	
	// NPC stuff
	
//	public void npcMove(SceneNode npc, float x, float y, float z)
//	{
//		float originX = x;
//		float originY = y;
//		float originZ = z;
//		
//		if (getDistanceBetween(Vector3f.createFrom(npc.getLocalPosition().x(), npc.getLocalPosition().y(), npc.getLocalPosition().z()), Vector3f.createFrom(originX, originY, originZ)) < 20.0f)
//		{
//			npc.setLocalPosition(npc.getLocalPosition().x() + 1.0f, npc.getLocalPosition().y(), npc.getLocalPosition().z());
//			npc.setLocalRotation(npc.getLocalRotation().inverse());
//		}
//	}
//	
//	public float getDistanceBetween(Vector3 a, Vector3 b)
//	{
//		return (float)Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2) + Math.pow(a.z() - b.z(), 2));
//	}
}
