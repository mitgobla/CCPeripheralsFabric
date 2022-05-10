package ccperipheralsfabric.common.peripheral.machine.vacuum;

import ccperipheralsfabric.CCPeripheralsFabric;
import ccperipheralsfabric.common.peripheral.machine.fan.TileFanMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockVacuumMachine extends BaseEntityBlock {
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    private static final BlockEntityTicker<TileVacuumMachine> serverTicker = ((level, blockPos, blockState, blockEntity) -> blockEntity.tick());
    private static final BlockEntityTicker<TileVacuumMachine> clientTicker = ((level, blockPos, blockState, blockEntity) -> blockEntity.tick());

    public BlockVacuumMachine(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(ENABLED, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
        stateManager.add(ENABLED);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(ENABLED, false);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TileVacuumMachine(blockPos, blockState);
    }

    @Nullable
    @Override
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<U> blockEntityType) {
        return level.isClientSide ? BaseEntityBlock.createTickerHelper(blockEntityType, CCPeripheralsFabric.TILE_VACUUM_MACHINE, clientTicker) : BaseEntityBlock.createTickerHelper( blockEntityType, CCPeripheralsFabric.TILE_VACUUM_MACHINE, serverTicker );
    }

}
