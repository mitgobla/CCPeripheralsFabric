package ccperipheralsfabric.common.peripheral.machine.led;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;


public class BlockLEDMachine extends Block implements BlockEntityProvider {
    public static final IntProperty COLOUR = IntProperty.of("colour", 0, 15);

    public BlockLEDMachine(FabricBlockSettings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(COLOUR, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COLOUR);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TileLEDMachine();
    }
}
