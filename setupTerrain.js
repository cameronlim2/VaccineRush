var JavaPackages = new JavaImporter(
    Packages.ray.rage.scene.SceneManager,
    Packages.ray.rage.scene.SceneNode,
    Packages.ray.rage.scene.Tessellation
);

with (JavaPackages) {
    var tessellationEntity, tessellationNode, tessellationEntity2, tessellationNode2;
    function setupTessellation(mygame) {
        tessellationEntity = mygame.getEngine().getSceneManager().createTessellation("tessellationEntity", 7);
        tessellationEntity.setSubdivisions(4);

        tessellationNode = mygame.getEngine().getSceneManager().getRootSceneNode().createChildSceneNode("tessellationNode");
        tessellationNode.attachObject(tessellationEntity);
        tessellationNode.translate(51.79, -1.0, -0.2);
        tessellationNode.scale(10.0, 50.0, 10.0);
        tessellationEntity.setHeightMap(mygame.getEngine(), "noisemap.jpg");
        tessellationEntity.setNormalMap(mygame.getEngine(), "noisemapnormal.png");
        tessellationEntity.setTexture(mygame.getEngine(), "grassTexture.jpg");


	tessellationEntity2 = mygame.getEngine().getSceneManager().createTessellation("tessellationEntity2", 7);
        tessellationEntity2.setSubdivisions(4);

        tessellation2Node = mygame.getEngine().getSceneManager().getRootSceneNode().createChildSceneNode("tessellationNode2");
        tessellation2Node.attachObject(tessellationEntity2);
        tessellation2Node.translate(-49.74, -1.0, -0.62);
        tessellation2Node.scale(10.0, 50.0, 10.0);
        tessellationEntity2.setHeightMap(mygame.getEngine(), "noisemap.jpg");
        tessellationEntity2.setNormalMap(mygame.getEngine(), "noisemapnormal.png");
        tessellationEntity2.setTexture(mygame.getEngine(), "grassTexture.jpg");
    }
}