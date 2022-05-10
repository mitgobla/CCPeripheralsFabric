package ccperipheralsfabric.client;

import ccperipheralsfabric.CCPeripheralsFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class CCPeripheralsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CCPeripheralsFabric.FAN_MACHINE, RenderType.cutout());
    }
}
