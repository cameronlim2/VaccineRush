package myGameEngine.ManualObject;

import ray.rage.Engine;
import ray.rage.rendersystem.Renderable.DataSource;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.SceneManager;
import ray.rage.util.BufferUtil;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.awt.Color;
import java.io.IOException;

public class ManualObjectFloor 
{
	public static ManualObject manualObjectFloor(Engine eng, SceneManager sm) throws IOException
	{
		ManualObject floor = sm.createManualObject("floorObj");
		ManualObjectSection floorSection = floor.createManualSection(floor.getName() + "Section");
		Material floorMat = sm.getMaterialManager().getAssetByPath("default.mtl");
		Texture floorT = eng.getTextureManager().getAssetByPath("tiledFloor.jpg");
				
		floor.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		int[] indices = { 0, 1, 2, 3, 4, 5};
		
		float[] floorVertices =
			{
					-1.0f, 0.0f, -1.0f,
					-1.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 1.0f,
					
					1.0f, 0.0f, 1.0f,
					1.0f, 0.0f, -1.0f,
					-1.0f, 0.0f, -1.0f
			};
		
		float[] floorNormals =
			{
					0.0f, 1.0f, 1.0f,
					0.0f, 1.0f, 1.0f,
					0.0f, 1.0f, 1.0f,
					0.0f, 1.0f, 1.0f,
					0.0f, 1.0f, 1.0f,
					0.0f, 1.0f, 1.0f,
			};
		
		float[] floorTextCoords =
			{
					1.0f, 1.0f,
					0.0f, 1.0f,
					0.0f, 0.0f,
					0.0f, 0.0f,
					1.0f, 0.0f,
					1.0f, 1.0f,
			};

		// Create Buffers
		FloatBuffer floorVertexBuffer = BufferUtil.directFloatBuffer(floorVertices);
		FloatBuffer floorNormalsBuffer = BufferUtil.directFloatBuffer(floorNormals);
		FloatBuffer floorTextCoordsBuffer = BufferUtil.directFloatBuffer(floorTextCoords);
		IntBuffer indicesBuffer = BufferUtil.directIntBuffer(indices);
		
		floorSection.setVertexBuffer(floorVertexBuffer);
		floorSection.setNormalsBuffer(floorNormalsBuffer);
		floorSection.setTextureCoordsBuffer(floorTextCoordsBuffer);
		floorSection.setIndexBuffer(indicesBuffer);
		
		floorMat.setEmissive(Color.BLACK);
		
		TextureState floorTS = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		floorTS.setTexture(floorT);
		
		FrontFaceState floorFrontFaceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
		
		floor.setDataSource(DataSource.INDEX_BUFFER);
		floor.setRenderState(floorTS);
		floor.setRenderState(floorFrontFaceState);
		
		return floor;
	}
}
