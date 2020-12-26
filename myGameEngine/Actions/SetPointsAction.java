package myGameEngine.Actions;

import ray.input.action.AbstractInputAction;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.scene.Entity;
import vaccineRush.VaccineRush;
import net.java.games.input.Event;

public class SetPointsAction extends AbstractInputAction
{
	private VaccineRush g;
	
	public SetPointsAction(VaccineRush g)
	{
		this.g = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		Entity dolphin = g.getEngine().getSceneManager().getEntity("dolphinE");
		dolphin.setPrimitive(Primitive.POINTS);
	}
}