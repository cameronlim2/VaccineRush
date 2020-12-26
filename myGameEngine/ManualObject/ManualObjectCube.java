package myGameEngine.ManualObject;

import ray.rage.Engine;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;
import ray.rage.rendersystem.Renderable.DataSource;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.rendersystem.states.FrontFaceState;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.scene.ManualObject;
import ray.rage.scene.ManualObjectSection;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rage.util.BufferUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.awt.Color;
import java.io.IOException;

public class ManualObjectCube 
{
	public static ManualObject makeCubeObject(Engine eng, SceneManager sm) throws IOException
	{
		
		ManualObject cubeObj = sm.createManualObject("CubeObj");
		ManualObjectSection cubeSection = cubeObj.createManualSection("CubeSection");
		cubeObj.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));
		
		float[] cubeVertices = 
			{
				
				-1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f,
				-1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f, 1.0f,		// left
				
				1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, -1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, -1.0f,		// right
				
				1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				-1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,		// back
				
				-1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,		// front
				
				1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f		// top

				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,		// bottom
				
			};
			
			float[] cubeTextureCoordinates = 
				{
						
				1.0f, 1.0f,
				0.0f, 1.0f, 
				0.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,		// left
				
				0.0f, 0.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 1.0f,
				1.0f, 0.0f,		// right
				
				0.0f, 0.0f, 
				1.0f, 1.0f, 
				0.0f, 1.0f, 
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 0.0f,		// back
				
				0.0f, 0.0f, 
				1.0f, 1.0f, 
				0.0f, 1.0f,
				1.0f, 1.0f, 
				0.0f, 0.0f, 
				1.0f, 0.0f,		// front
				
				
			};
			
			float[] cubeNormals = 
				{
						
				-1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,		// left
				
				1.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,			
				1.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f,		// right
				
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,		// front
				
				0.0f, 1.0f, -1.0f,
				0.0f, 1.0f, -1.0f, 
				0.0f, 1.0f, -1.0f,
				0.0f, 1.0f, -1.0f,
				0.0f, 1.0f, -1.0f, 
				0.0f, 1.0f, -1.0f,		// back
				
				0.0f, -1.0f, -1.0f,
				0.0f, -1.0f, -1.0f,
				0.0f, -1.0f, -1.0f,
				0.0f, -1.0f, -1.0f,
				0.0f, -1.0f, -1.0f,
				0.0f, -1.0f, -1.0f,		// bottom
				
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f,
				0.0f, 1.0f, 1.0f		// top
			};
			
			int[] cubeIndices = {
				0, 1, 2, 3, 4, 5,
				6, 7, 8, 9, 10, 11,
				12, 13, 14, 15, 16, 17,
				18, 19, 20, 21, 22, 23,
				24, 25, 26, 27, 28, 29,
				30, 31, 32, 33, 34, 35
			};
			
			FloatBuffer cubeVertexBuffer = BufferUtil.directFloatBuffer(cubeVertices);
			FloatBuffer cubeTextureCoordinateBuffer = BufferUtil.directFloatBuffer(cubeTextureCoordinates);
			FloatBuffer cubeNormalBuffer = BufferUtil.directFloatBuffer(cubeNormals);
			IntBuffer cubeIndicesBuffer = BufferUtil.directIntBuffer(cubeIndices);
			
			cubeSection.setVertexBuffer(cubeVertexBuffer);
			cubeSection.setTextureCoordsBuffer(cubeTextureCoordinateBuffer);
			cubeSection.setNormalsBuffer(cubeNormalBuffer);
			cubeSection.setIndexBuffer(cubeIndicesBuffer);
			
			Texture chainFenceT = eng.getTextureManager().getAssetByPath("chain-fence.jpeg");
			TextureState cubeTextureState = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
			cubeTextureState.setTexture(chainFenceT);
			
			FrontFaceState cubeFrontFaceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
			cubeFrontFaceState.setVertexWinding(FrontFaceState.VertexWinding.COUNTER_CLOCKWISE);
			
			cubeObj.setDataSource(DataSource.INDEX_BUFFER);
			cubeObj.setRenderState(cubeTextureState);
			cubeObj.setRenderState(cubeFrontFaceState);
			
			return cubeObj;
			
	}
}
