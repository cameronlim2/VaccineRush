package myGameEngine.ManualObject;

import ray.rage.Engine;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.rendersystem.Renderable.DataSource;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.util.BufferUtil;

import java.awt.Color;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// Creating 3 lines to represent the Cartesian XYZ plane of the game world. Each line
// will be colored differently (RBG)

public class ManualObjectAxisLine 
{
	private static ManualObject makeAxisLine(Engine eng, SceneManager sm, char axis) throws IOException
	{
		ManualObject line = sm.createManualObject((axis + "Line"));
		ManualObjectSection lineSection = line.createManualSection(axis + "LineSection");
		line.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
	
		Material lineMaterial = sm.getMaterialManager().getAssetByPath("default.mtl");
		Texture lineTexture;
		float[] lineVertices = new float[6];
		
		float[] lineTextureCoordinates =
			{
					0.0f, 0.0f, 0.0f,
					0.0f, 100.0f, 0.0f
			};
		
		float[] lineNormals= 
			{
					0.0f, 0.0f, 0.1f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f
			};
		
		int[] indices = { 0, 1 };
		
		switch (axis)
		{
		case 'X': // X - Red
				lineVertices[3] = 100.0f;
				lineMaterial.setEmissive(Color.RED);
				lineTexture = eng.getTextureManager().getAssetByPath("red_axis.png");
				break;
			
		case 'Y': // Y - Green
				lineVertices[4] = 100.0f;
				lineMaterial.setEmissive(Color.GREEN);
				lineTexture = eng.getTextureManager().getAssetByPath("green_axis.png");
				break;
		
		default: // Z - Blue
				lineVertices[5] = 100.0f;
				lineMaterial.setEmissive(Color.BLUE);
				lineTexture = eng.getTextureManager().getAssetByPath("blue_axis.png");
		}
		
		FloatBuffer lineVertexBuffer = BufferUtil.directFloatBuffer(lineVertices);
		FloatBuffer lineTextureBuffer = BufferUtil.directFloatBuffer(lineTextureCoordinates);
		FloatBuffer lineNormalsBuffer = BufferUtil.directFloatBuffer(lineNormals);
		IntBuffer lineIndicesBuffer = BufferUtil.directIntBuffer(indices);
		
		lineSection.setVertexBuffer(lineVertexBuffer);
		lineSection.setTextureCoordsBuffer(lineTextureBuffer);
		lineSection.setNormalsBuffer(lineNormalsBuffer);
		lineSection.setIndexBuffer(lineIndicesBuffer);
		
		TextureState lineTextureState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		lineTextureState.setTexture(lineTexture);
		
		FrontFaceState lineFrontFaceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		
		line.setRenderState(lineFrontFaceState);
		line.setRenderState(lineTextureState);
		
		return line;
	}
	
	public static void renderWorldAxes(Engine eng, SceneManager sm) throws IOException
	{
		
		SceneNode xAxisN, yAxisN, zAxisN;
		
		ManualObject xAxis = makeAxisLine(eng, sm, 'X');
		ManualObject yAxis = makeAxisLine(eng, sm, 'Y');
		ManualObject zAxis = makeAxisLine(eng, sm, 'Z');
		
		xAxis.setPrimitive(Primitive.LINES);
		yAxis.setPrimitive(Primitive.LINES);
		zAxis.setPrimitive(Primitive.LINES);
		
		xAxisN = sm.getRootSceneNode().createChildSceneNode("XAxisLine");
		yAxisN = sm.getRootSceneNode().createChildSceneNode("YAxisLine");
		zAxisN = sm.getRootSceneNode().createChildSceneNode("ZAxisLine");
		
		xAxisN.attachObject(xAxis);
		yAxisN.attachObject(yAxis);
		zAxisN.attachObject(zAxis);
		
		xAxisN.scale(1.0f, 1.0f, 1.0f);
		yAxisN.scale(1.0f, 1.0f, 1.0f);
		zAxisN.scale(1.0f, 1.0f, 1.0f);
		
		xAxisN.setLocalPosition(0.0f, 0.0f, 0.0f);
		yAxisN.setLocalPosition(0.0f, 0.0f, 0.0f);
		zAxisN.setLocalPosition(0.0f, 0.0f, 0.0f);
		
	}
}
