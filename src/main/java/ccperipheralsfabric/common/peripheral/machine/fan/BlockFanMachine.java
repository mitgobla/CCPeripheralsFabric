package ccperipheralsfabric.common.peripheral.machine.fan;

import ccperipheralsfabric.CCPeripheralsFabric;
import dan200.computercraft.shared.Registry;
import dan200.computercraft.shared.common.BlockGeneric;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlockFanMachine extends BaseEntityBlock  {
    public static final BooleanProperty ENABLED = BooleanProperty.create("enabled");

    public static final DirectionProperty FACING;

    private static final BlockEntityTicker<TileFanMachine> serverTicker = ((level, blockPos, blockState, blockEntity) -> blockEntity.tick());
    private static final BlockEntityTicker<TileFanMachine> clientTicker = ((level, blockPos, blockState, blockEntity) -> blockEntity.tick());

    public BlockFanMachine(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.FACING, Direction.SOUTH).setValue(ENABLED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING, ENABLED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) this.defaultBlockState().setValue(BlockStateProperties.FACING, ctx.getNearestLookingDirection().getOpposite()).setValue(ENABLED, false);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TileFanMachine(blockPos, blockState);
    }

    @Nullable
    @Override
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<U> blockEntityType) {
        return level.isClientSide ? BaseEntityBlock.createTickerHelper(blockEntityType, CCPeripheralsFabric.TILE_FAN_MACHINE, clientTicker) : BaseEntityBlock.createTickerHelper( blockEntityType, CCPeripheralsFabric.TILE_FAN_MACHINE, serverTicker );
    }

    static {
        FACING = BlockStateProperties.FACING;
    }
}
