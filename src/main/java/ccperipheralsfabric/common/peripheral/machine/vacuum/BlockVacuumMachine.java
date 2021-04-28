package ccperipheralsfabric.common.peripheral.machine.vacuum;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class BlockVacuumMachine extends Block implements BlockEntityProvider {
    public static final BooleanProperty ENABLED = BooleanProperty.of("enabled");

    public BlockVacuumMachine(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(ENABLED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(ENABLED);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState) this.getDefaultState().with(ENABLED, false);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new TileVacuumMachine();
    }

}
